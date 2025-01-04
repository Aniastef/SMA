package com.example.triviagame

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Question(
    val id: String,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun TriviaGame(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    val tiltDetector = remember { TiltDetector(context) }
    val tiltDirection = tiltDetector.tiltDirection

    DisposableEffect(Unit) {
        tiltDetector.startListening()
        onDispose { tiltDetector.stopListening() }
    }

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]

    var selectedAnswer by rememberSaveable { mutableStateOf<String?>(null) }
    var isAnswerCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var answerLocked by rememberSaveable { mutableStateOf(false) }

    fun saveProgress(questionId: String, isCorrect: Boolean) {
        if (currentUser == null) {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val userDoc = db.collection("users").document(currentUser.uid)
        userDoc.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val initialData = mapOf(
                    "correctAnswers" to 0,
                    "answeredQuestions" to emptyList<String>()
                )
                userDoc.set(initialData).addOnSuccessListener {
                    saveProgress(questionId, isCorrect)
                }
                return@addOnSuccessListener
            }

            val answeredQuestions = (document.get("answeredQuestions") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            if (isCorrect && questionId !in answeredQuestions) {
                val currentCount = document.getLong("correctAnswers") ?: 0
                val updatedAnsweredQuestions = answeredQuestions.toMutableList().apply { add(questionId) }

                userDoc.update(
                    mapOf(
                        "correctAnswers" to currentCount + 1,
                        "answeredQuestions" to updatedAnsweredQuestions
                    )
                ).addOnSuccessListener {
                    Toast.makeText(context, "Progress saved!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { error ->
                    Toast.makeText(context, "Failed to save progress: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error fetching Firestore data: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(tiltDirection.value) {
        if (!answerLocked) {
            val newSelectedAnswer = when (tiltDirection.value) {
                "Left" -> currentQuestion.answers.getOrNull(0)
                "Right" -> currentQuestion.answers.getOrNull(1)
                else -> null
            }
            if (newSelectedAnswer != null && newSelectedAnswer != selectedAnswer) {
                selectedAnswer = newSelectedAnswer
                isAnswerCorrect = selectedAnswer == currentQuestion.correctAnswer
                answerLocked = true
                saveProgress(currentQuestion.id, isAnswerCorrect == true)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question ${currentQuestionIndex + 1} of ${questions.size}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.wrapContentSize(Alignment.Center)) {
            Button(onClick = { expanded = true }) { Text("Go to Question") }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                questions.forEachIndexed { index, _ ->
                    DropdownMenuItem(
                        text = { Text("Question ${index + 1}") },
                        onClick = {
                            currentQuestionIndex = index
                            selectedAnswer = null
                            isAnswerCorrect = null
                            answerLocked = false
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(currentQuestion.question, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        currentQuestion.answers.forEachIndexed { index, answer ->
            Text("${index + 1}. $answer", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        selectedAnswer?.let {
            Text(
                if (isAnswerCorrect == true) "That is correct!" else "Wrong! The correct answer is ${currentQuestion.correctAnswer}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (currentQuestionIndex > 0) {
                        currentQuestionIndex--
                        selectedAnswer = null
                        isAnswerCorrect = null
                        answerLocked = false
                    }
                },
                enabled = currentQuestionIndex > 0
            ) { Text("Last Question") }

            Button(
                onClick = {
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                        selectedAnswer = null
                        isAnswerCorrect = null
                        answerLocked = false
                    } else {
                        onFinished()
                    }
                }
            ) { Text(if (currentQuestionIndex == questions.size - 1) "End" else "Next Question") }
        }
    }
}
