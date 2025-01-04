package com.example.triviagame

import NavBar
import HomePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.triviagame.ui.theme.TriviaGameTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            TriviaGameTheme {
                MainApp(auth)
            }
        }
    }
}

@Composable
fun MainApp(auth: FirebaseAuth) {
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    var showLoginScreen by remember { mutableStateOf(true) }

    if (isUserLoggedIn) {
        GameApp(onLogout = {
            auth.signOut()
            isUserLoggedIn = false
            showLoginScreen = true
        })
    } else {
        if (showLoginScreen) {
            LoginScreen(
                auth = auth,
                onNavigateToSignUp = { showLoginScreen = false },
                onLoginSuccessful = {
                    isUserLoggedIn = true
                    showLoginScreen = false
                }
            ).Content()
        } else {
            SignupScreen(
                auth = auth,
                onNavigateToLogin = { showLoginScreen = true }
            ).Content()
        }
    }
}

@Composable
fun GameApp(onLogout: () -> Unit) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    var selectedQuestions by rememberSaveable { mutableStateOf<List<Question>?>(null) }
    var selectedUser by remember { mutableStateOf<UserRank?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NavBar(
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                    selectedQuestions = null
                    selectedUser = null
                },
                onLogout = {
                    onLogout()
                }
            )
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> {
                if (selectedQuestions == null) {
                    HomePage(
                        onCategorySelected = { questions ->
                            selectedQuestions = questions
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    TriviaGame(
                        questions = selectedQuestions ?: emptyList(),
                        modifier = Modifier.padding(innerPadding),
                        onFinished = { selectedQuestions = null }
                    )
                }
            }
            1 -> {
                if (selectedUser == null) {
                    UserRankingPage(
                        modifier = Modifier.padding(innerPadding),
                        onUserClick = { user ->
                            selectedUser = user
                        }
                    )
                } else {
                    ProfilePage(
                        user = selectedUser!!,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            2 -> ProfilePage()
        }
    }
}

