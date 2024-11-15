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

class SignupScreen(
    private val auth: FirebaseAuth,
    private val onNavigateToLogin: () -> Unit
) {
    @Composable
    fun Content() {
        val context = LocalContext.current

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Create Account", fontSize = 32.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(20.dp))

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }
                var isLoading by remember { mutableStateOf(false) }

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() }, // Trim spaces
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

                Spacer(modifier = Modifier.height(10.dp))

                // Confirm Password TextField
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Confirm Password Icon") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPassword.isNotBlank() && confirmPassword != password,
                    modifier = Modifier.fillMaxWidth()
                )
                if (confirmPassword.isNotBlank() && confirmPassword != password) {
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Show loading spinner or sign-up button
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
                            if (password.length < 6) {
                                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (password != confirmPassword) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            auth.createUserWithEmailAndPassword(trimmedEmail, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                                        onNavigateToLogin() // Navigate back to LoginScreen
                                    } else {
                                        val errorMessage = task.exception?.message ?: "Sign Up Failed"
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                    ) {
                        Text(text = "Sign Up")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login", color = Color.Blue)
                }
            }
        }
    }
}
