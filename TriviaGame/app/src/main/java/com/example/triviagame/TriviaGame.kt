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

    // using accelerometer
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
                // create user document if it doesn t exist
                val initialData = mapOf(
                    "correctAnswers" to 0,
                    "answeredQuestions" to emptyList<String>()
                )
                userDoc.set(initialData).addOnSuccessListener {
                    //Toast.makeText(context, "User document created", Toast.LENGTH_SHORT).show()
                    // save progress after creating document
                    saveProgress(questionId, isCorrect)
                }.addOnFailureListener { //error ->
                    //Toast.makeText(context, "Failed to create user document: ${error.message}", Toast.LENGTH_LONG).show()
                }
                return@addOnSuccessListener
            }

            // the document exists
            val answeredQuestions = (document.get("answeredQuestions") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

            if (isCorrect && questionId !in answeredQuestions) {
                val currentCount = document.getLong("correctAnswers") ?: 0

                val updatedAnsweredQuestions = answeredQuestions.toMutableList().apply {
                    add(questionId)
                }

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


    // choose answer by rotating your phone
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
                if (isAnswerCorrect == true) {
                    saveProgress(currentQuestion.id, true)
                }
                answerLocked = true
            }
        }
    }

    // UI
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // question
        Text(
            text = currentQuestion.question,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // answers
        currentQuestion.answers.forEachIndexed { index, answer ->
            Text(
                text = "${index + 1}. $answer",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // instructions
        if (!answerLocked) {
            Text(
                text = "Tilt your phone to the left to choose answer 1 and to the right to choose answer 2",
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // selected answer and answer reveal
        selectedAnswer?.let {
            Text(
                text = if (isAnswerCorrect == true) "That is correct!" else "Wrong! The correct answer is ${currentQuestion.correctAnswer}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // next question or trivia end
        val isLastQuestion = currentQuestionIndex == questions.size - 1

        Button(onClick = {
            if (isLastQuestion) {
                onFinished()
            } else {
                currentQuestionIndex++
                selectedAnswer = null
                isAnswerCorrect = null
                answerLocked = false
            }
        }) {
            Text(text = if (isLastQuestion) "End" else "Next question")
        }
    }
}
