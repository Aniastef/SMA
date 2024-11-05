import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.triviagame.TiltDetector

@Composable
fun TriviaGame() {
    val context = LocalContext.current
    val tiltDetector = remember { TiltDetector(context) }
    val tiltDirection = tiltDetector.tiltDirection

    DisposableEffect(Unit) {
        tiltDetector.startListening()
        onDispose { tiltDetector.stopListening() }
    }

    //intrebare
    val question = "Care este capitala Franței?"
    val answers = listOf("Paris", "Berlin", "Roma", "Madrid")
    val correctAnswer = "Paris"

    //verifica directia de inclinare pt setare raspuns
    val selectedAnswer = when (tiltDirection.value) {
        "Up" -> answers[0] 
        "Down" -> answers[1] 
        "Left" -> answers[2] 
        "Right" -> answers[3] 
        else -> null
    }

    //UI
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        //afisarea raspunsurilor
        answers.forEachIndexed { index, answer ->
            Text(text = "${index + 1}. $answer", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // afisarea directiei detectate
        Text(text = "Tilt direction: ${tiltDirection.value}")

        //afisare raspuns selectat
        selectedAnswer?.let {
            Text(
                text = if (it == correctAnswer) "Corect!" else "Greșit! Răspunsul era: $correctAnswer",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
