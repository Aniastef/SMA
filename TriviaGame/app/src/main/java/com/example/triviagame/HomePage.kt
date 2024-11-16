import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviagame.Question

@Composable
fun HomePage(onCategorySelected: (List<Question>) -> Unit, modifier: Modifier = Modifier) {

    val historyQuestions = listOf(
        Question("q1", "Who was the first president of the United States?", listOf("George Washington", "Thomas Jefferson"), "George Washington"),
        Question("q2", "In which year did World War I begin?", listOf("1914", "1939"), "1914"),
        Question("q3", "What is the capital of ancient Rome?", listOf("Rome", "Athens"), "Rome"),
        Question("q10", "What was the name of the war in the USA from 1861-1865?", listOf("Civil War", "War of Independence"), "Civil War"),
        Question("q31", "Who was known as 'The Great' in Macedonian history?", listOf("Alexander the Great", "Philip II"), "Alexander the Great")
    )

    val scienceQuestions = listOf(
        Question("q4", "What element has the chemical symbol 'O'?", listOf("Oxygen", "Gold"), "Oxygen"),
        Question("q5", "What is the speed of light?", listOf("300,000 km/s", "150,000 km/s"), "300,000 km/s"),
        Question("q6", "Which planet is closest to the Sun?", listOf("Mercury", "Venus"), "Mercury"),
        Question("q11", "What gas is essential for human respiration?", listOf("Oxygen", "Hydrogen"), "Oxygen"),
        Question("q32", "Who developed the theory of general relativity?", listOf("Albert Einstein", "Isaac Newton"), "Albert Einstein")
    )

    val geographyQuestions = listOf(
        Question("q7", "What is the largest ocean?", listOf("Pacific Ocean", "Atlantic Ocean"), "Pacific Ocean"),
        Question("q8", "Which country has the largest population?", listOf("China", "India"), "China"),
        Question("q9", "In which continent is Egypt located?", listOf("Africa", "Asia"), "Africa"),
        Question("q12", "What is the longest river in the world?", listOf("Amazon", "Nile"), "Amazon"),
        Question("q33", "Which desert is the largest in the world?", listOf("Sahara", "Gobi"), "Sahara")
    )

    val entertainmentQuestions = listOf(
        Question("q13", "Who directed the movie 'Titanic'?", listOf("James Cameron", "Steven Spielberg"), "James Cameron"),
        Question("q14", "Who created the Harry Potter character?", listOf("J.K. Rowling", "Stephen King"), "J.K. Rowling"),
        Question("q15", "Which artist is known for the album 'Thriller'?", listOf("Michael Jackson", "Prince"), "Michael Jackson"),
        Question("q34", "Who played the main role in the movie 'Forrest Gump'?", listOf("Tom Hanks", "Brad Pitt"), "Tom Hanks"),
        Question("q35", "What is the name of the fictional country in 'Black Panther'?", listOf("Wakanda", "Zamunda"), "Wakanda")
    )

    val sportsQuestions = listOf(
        Question("q16", "Which country has won the most FIFA World Cups?", listOf("Brazil", "Germany"), "Brazil"),
        Question("q17", "What is the distance of a marathon?", listOf("42 km", "50 km"), "42 km"),
        Question("q18", "Which sport uses the term 'love' for scoring?", listOf("Tennis", "Badminton"), "Tennis"),
        Question("q36", "Who holds the record for the most Olympic gold medals?", listOf("Michael Phelps", "Usain Bolt"), "Michael Phelps"),
        Question("q37", "Which team won the NBA Championship in 2020?", listOf("Los Angeles Lakers", "Miami Heat"), "Los Angeles Lakers")
    )

    val technologyQuestions = listOf(
        Question("q19", "Who is the founder of Microsoft?", listOf("Bill Gates", "Steve Jobs"), "Bill Gates"),
        Question("q20", "What does AI stand for?", listOf("Artificial Intelligence", "Automated Intelligence"), "Artificial Intelligence"),
        Question("q21", "Which programming language is used for Android development?", listOf("Kotlin", "Swift"), "Kotlin"),
        Question("q38", "What does HTTP stand for?", listOf("HyperText Transfer Protocol", "High Transfer Protocol"), "HyperText Transfer Protocol"),
        Question("q39", "What year was the first iPhone released?", listOf("2007", "2010"), "2007")
    )

    //
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select a category of questions",
            fontSize = 24.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = { onCategorySelected(historyQuestions) }) {
            Text(text = "History")
        }

        Button(onClick = { onCategorySelected(scienceQuestions) }) {
            Text(text = "Science")
        }

        Button(onClick = { onCategorySelected(geographyQuestions) }) {
            Text(text = "Geography")
        }

        Button(onClick = { onCategorySelected(entertainmentQuestions) }) {
            Text(text = "Entertainment")
        }

        Button(onClick = { onCategorySelected(sportsQuestions) }) {
            Text(text = "Sports")
        }

        Button(onClick = { onCategorySelected(technologyQuestions) }) {
            Text(text = "Technology")
        }
    }
}
