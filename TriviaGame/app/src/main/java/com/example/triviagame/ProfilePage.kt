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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.triviagame.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Retrieve full name and email
    val fullName = currentUser?.displayName ?: "Unknown Name"
    val email = currentUser?.email ?: "Unknown Email"

    // State to store selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Context for Toast
    val context = LocalContext.current

    // Launcher for selecting an image from the gallery
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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Display selected image or hamster placeholder
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.hamster), // Replace with your hamster image ID
                    contentDescription = "Default Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to select a profile picture
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(text = "Upload Profile Picture")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display full name and email
            Text(
                text = fullName,
                fontSize = 24.sp
            )
            Text(
                text = email,
                fontSize = 16.sp
            )
        }
    }
}
