package com.example.triviagame

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class UserRank(val name: String, val email: String, val score: Int)


@Composable
fun UserRankingPage(
    modifier: Modifier = Modifier,
    onUserClick: (UserRank) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    var users by remember { mutableStateOf<List<UserRank>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("users")
            .orderBy("correctAnswers", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userList = querySnapshot.documents.mapNotNull { document ->
                    val name = document.getString("displayName") ?: "Unknown"
                    val email = document.getString("email") ?: "Unknown"
                    val score = document.getLong("correctAnswers")?.toInt() ?: 0
                    UserRank(name, email, score)
                }
                users = userList
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Users with most correct answers",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (users.isEmpty()) {
            Text(
                text = "No users.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(users) { index, user ->
                    UserRankingItem(
                        position = index + 1,
                        user = user,
                        onClick = { onUserClick(user) }
                    )
                }
            }
        }
    }
}

@Composable
fun UserRankingItem(
    position: Int,
    user: UserRank,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() } // Detect click
    ) {
        Text(
            text = "$position",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.width(30.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Text(
                text = "${user.score} correct answers",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
