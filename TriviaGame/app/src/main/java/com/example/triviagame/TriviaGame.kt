package com.example.triviagame
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun TriviaGame(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit // callback care se apeleaza la ultima intrebare
) {
    val context = LocalContext.current
    val tiltDetector = remember { TiltDetector(context) }
    val tiltDirection = tiltDetector.tiltDirection

    DisposableEffect(Unit) {
        tiltDetector.startListening()
        onDispose { tiltDetector.stopListening() }
    }

    var currentQuestionIndex by rememberSaveable { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]

    var selectedAnswer by rememberSaveable { mutableStateOf<String?>(null) }
    var isAnswerCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var answerLocked by rememberSaveable { mutableStateOf(false) }

    val progress = (currentQuestionIndex + 1) / questions.size.toFloat()

    LaunchedEffect(tiltDirection.value) {
        if (!answerLocked) {
            val newSelectedAnswer = when (tiltDirection.value) {
                "Left" -> currentQuestion.answers[0]
                "Right" -> currentQuestion.answers[1]
                else -> null
            }
            if (newSelectedAnswer != null && newSelectedAnswer != selectedAnswer) {
                selectedAnswer = newSelectedAnswer
                isAnswerCorrect = selectedAnswer == currentQuestion.correctAnswer
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
        Text(text = currentQuestion.question,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        currentQuestion.answers.forEachIndexed { index, answer ->
            Text(text = "${index + 1}. $answer", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (!answerLocked) {
            Text(text = "Rotește telefonul în stânga pentru varianta 1 și în dreapta pentru varianta 2", textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedAnswer?.let {
            Text(
                text = if (isAnswerCorrect == true) "Corect! Răspunsul este: ${currentQuestion.correctAnswer}" else "Greșit! Răspunsul era: ${currentQuestion.correctAnswer}",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val isLastQuestion = currentQuestionIndex == questions.size - 1

        Button(onClick = {
            if (isLastQuestion) {
                onFinished() // revine la homepage la ultima intrebare
            } else {
                //urmatoarea intrebare
                currentQuestionIndex++
                selectedAnswer = null
                isAnswerCorrect = null
                answerLocked = false
            }
        }) {
            Text(text = if (isLastQuestion) "Înapoi spre categorii" else "Următoarea întrebare")
        }

        Spacer(modifier = Modifier.height(30.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .height(20.dp)
                .padding(horizontal = 30.dp),
            trackColor = Color.LightGray,
        )
    }
}
