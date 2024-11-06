package com.example.triviagame
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviagame.ui.theme.TriviaGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TriviaGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TriviaGame(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val tiltDetector = remember { TiltDetector(context) }
    val tiltDirection = tiltDetector.tiltDirection

    DisposableEffect(Unit) {
        tiltDetector.startListening()
        onDispose { tiltDetector.stopListening() }
    }

    // Întrebarea și răspunsurile
    val question = "Care este capitala Franței?"
    val answers = listOf("Paris", "Berlin", "Roma", "Madrid")
    val correctAnswer = "Paris"

    // Verifică direcția de înclinare pentru a selecta un răspuns
    val selectedAnswer = when (tiltDirection.value) {
        "Up" -> answers[0]
        "Down" -> answers[1]
        "Left" -> answers[2]
        "Right" -> answers[3]
        else -> null
    }

    // UI-ul principal
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Afișează răspunsurile
        answers.forEachIndexed { index, answer ->
            Text(text = "${index + 1}. $answer", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Afișează direcția detectată
        Text(text = "Tilt direction: ${tiltDirection.value}")

        // Afișează răspunsul selectat
        selectedAnswer?.let {
            Text(
                text = if (it == correctAnswer) "Corect!" else "Greșit! Răspunsul era: $correctAnswer",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TriviaGamePreview() {
    TriviaGameTheme {
        TriviaGame()
    }
}
