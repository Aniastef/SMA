package com.example.triviagame
import HomePage
import NavBar
import ProfilePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.triviagame.ui.theme.TriviaGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaGameTheme {
                var selectedItem by rememberSaveable { mutableStateOf(0) }
                var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
                val historyQuestions = listOf(
                    Question("Cine a fost primul președinte al Statelor Unite?", listOf("George Washington", "Thomas Jefferson"), "George Washington"),
                    Question("În ce an a început Primul Război Mondial?", listOf("1914", "1939"), "1914"),
                    Question("Care este capitala Romei antice?", listOf("Roma", "Atena"), "Roma")
                )

                val scienceQuestions = listOf(
                    Question("Ce element are simbolul chimic 'O'?", listOf("Oxigen", "Aur"), "Oxigen"),
                    Question("Care este viteza luminii?", listOf("300,000 km/s", "150,000 km/s"), "300,000 km/s"),
                    Question("Ce planetă este cea mai apropiată de Soare?", listOf("Mercur", "Venus"), "Mercur")
                )

                val geographyQuestions = listOf(
                    Question("Care este cel mai mare ocean?", listOf("Oceanul Pacific", "Oceanul Atlantic"), "Oceanul Pacific"),
                    Question("Ce țară are cea mai mare populație?", listOf("China", "India"), "China"),
                    Question("În ce continent se află Egiptul?", listOf("Africa", "Asia"), "Africa")
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        NavBar(
                            selectedItem = selectedItem,
                            onItemSelected = { index ->
                                selectedItem = index
                                selectedCategory = null
                            }
                        )
                    }
                ) { innerPadding ->
                    when (selectedItem) {
                        0 -> {
                            if (selectedCategory == null) {
                                HomePage(
                                    onCategorySelected = { category ->
                                        selectedCategory = category
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            } else {

                                TriviaGame(
                                    questions = when (selectedCategory) {
                                        "Istorie" -> historyQuestions
                                        "Știință" -> scienceQuestions
                                        "Geografie" -> geographyQuestions
                                        else -> emptyList()
                                    },
                                    modifier = Modifier.padding(innerPadding),
                                    onFinished = { selectedCategory = null }
                                )
                            }
                        }
                        1 -> UserRankingPage(modifier = Modifier.padding(innerPadding))
                        2 -> ProfilePage(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

