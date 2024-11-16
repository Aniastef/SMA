package com.example.triviagame
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

class LoginScreen(
    private val auth: FirebaseAuth,
    private val onNavigateToSignUp: () -> Unit,
    private val onLoginSuccessful: () -> Unit
) {
    @Composable
    fun Content() {
        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back",
                    fontSize = 32.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email Icon") },
                    isError = email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Text(
                        text = "Invalid email address",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password Icon") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = password.isNotBlank() && password.length < 6,
                    modifier = Modifier.fillMaxWidth()
                )
                if (password.isNotBlank() && password.length < 6) {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Show loading spinner or login button
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            val trimmedEmail = email.trim()
                            if (trimmedEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (password.isBlank() || password.length < 6) {
                                Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            auth.signInWithEmailAndPassword(trimmedEmail, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                        onLoginSuccessful() // Trigger success callback
                                    } else {
                                        Toast.makeText(
                                            context,
                                            task.exception?.message ?: "Login Failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotBlank() && password.isNotBlank()
                    ) {
                        Text(text = "Login")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Navigate to SignUp
                TextButton(onClick = onNavigateToSignUp) {
                    Text("Don't have an account? Register", color = Color.Blue)
                }
            }
        }
    }
}
