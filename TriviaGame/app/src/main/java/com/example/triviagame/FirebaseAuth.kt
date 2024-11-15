package com.example.triviagame

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class FirebaseAuth {

    private val auth = Firebase.auth

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onComplete(true, null) // Success
                } else {
                    onComplete(false, task.exception?.message) // Error message
                }
            }
    }

    fun getCurrentUser() = auth.currentUser
}
