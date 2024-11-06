package com.example.triviagame

import NavBar
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { NavBar() }
                ) { innerPadding ->
                    TriviaGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
