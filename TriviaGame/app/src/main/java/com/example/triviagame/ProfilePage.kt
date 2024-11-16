package com.example.triviagame

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//  ProfilePage for logged-in user
@Composable
fun ProfilePage() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    // User profile data state
    var fullName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // For profile picture

    val context = LocalContext.current

    // reload and synchronize data from FirebaseAuth and Firestore
    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            user.reload().addOnCompleteListener { reloadTask ->
                if (reloadTask.isSuccessful) {
                    fullName = user.displayName ?: "Unknown Name"
                    email = user.email ?: "Unknown Email"

                    // fetch correct answers count from Firestore
                    db.collection("users").document(user.uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                correctAnswersCount = document.getLong("correctAnswers")?.toInt() ?: 0
                            } else {
                                // initialize Firestore data if not present
                                val initialData = mapOf(
                                    "displayName" to fullName,
                                    "correctAnswers" to 0
                                )
                                db.collection("users").document(user.uid).set(initialData)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to fetch correct answers.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Failed to reload user data.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // UI
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // image/placeholder
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.hamster),
                    contentDescription = "Default Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // upload new profile picture
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                selectedImageUri = uri
                if (uri != null) {
                    Toast.makeText(
                        context,
                        "Profile picture updated!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(text = "Upload Profile Picture")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // user data
            Text(
                text = fullName,
                fontSize = 24.sp
            )
            Text(
                text = email,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Correct answers: $correctAnswersCount",
                fontSize = 20.sp
            )
        }
    }
}

// ProfilePage for other uses
@Composable
fun ProfilePage(user: UserRank, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hamster),
                contentDescription = "Default Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "Profile of ${user.name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${user.email}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${user.score} correct answers",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}
