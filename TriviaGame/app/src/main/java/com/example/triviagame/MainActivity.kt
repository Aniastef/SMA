package com.example.triviagame

import NavBar
import ProfilePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.triviagame.ui.theme.TriviaGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaGameTheme {
                var selectedItem by remember { mutableStateOf(0) } // Stare pentru pagina selectată

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        NavBar(
                            selectedItem = selectedItem,
                            onItemSelected = { selectedItem = it }
                        )
                    }
                ) { innerPadding ->
                    when (selectedItem) {
                        0 -> TriviaGame(modifier = Modifier.padding(innerPadding))
                        1 -> UserRankingPage(modifier = Modifier.padding(innerPadding))
                        2 -> ProfilePage(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
