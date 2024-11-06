package com.example.triviagame
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun TriviaGame(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val tiltDetector = remember { TiltDetector(context) }
    val tiltDirection = tiltDetector.tiltDirection

    DisposableEffect(Unit) {
        tiltDetector.startListening()
        onDispose { tiltDetector.stopListening() }
    }

    //intrebari si raspunsuri-*------************************----33
    val questions = listOf(
        Question("Care este capitala Franței?", listOf("Paris", "Berlin"), "Paris"),
        Question("Care este cel mai înalt munte din lume?", listOf("Kilimanjaro", "Everest"), "Everest"),
        Question("Ce planetă este cunoscută drept Planeta Roșie?", listOf("Marte", "Venus"), "Marte")
    )

    // stat pentru întrebarea curentă
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]

    val answers = listOf("Paris", "Berlin")
    val correctAnswer = "Paris"

    // se verifica directia de inclinare a telefonului pentru selectarea unui raspuns
    val selectedAnswer = when (tiltDirection.value) {
        "Left" -> answers[0]
        "Right" -> answers[1]
        else -> null
    }

     // UI-ul principal
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = currentQuestion.question,
            style = MaterialTheme.typography.headlineMedium,
            textAlign= TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // se afiseaza raspunsurile
        currentQuestion.answers.forEachIndexed { index, answer ->
            Text(text = "${index + 1}. $answer",
                style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // afiseaza directia selectata
        Text(
            text = "Rotește telefonul în stânga pentru varianta 1 și în dreapta pentru varianta 2",
            textAlign = TextAlign.Center
        )

        // afiseaza corect sau gresit
        selectedAnswer?.let {
            Text(
                text = if (it == currentQuestion.correctAnswer) "Corect! Răspunsul este: ${currentQuestion.correctAnswer}" else "Greșit! Răspunsul era: ${currentQuestion.correctAnswer}",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // buton pentru a trece la următoarea întrebare
        Button(onClick = {
            currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        }) {
            Text(text = "Următoarea întrebare")
        }
    }
}