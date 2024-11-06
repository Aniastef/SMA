package com.example.triviagame
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class User(val name: String, val score: Int)

@Composable
fun UserRankingPage(modifier: Modifier = Modifier) {

    //lista fictiva
    val users = listOf(
        User("Tălăpan Alexandra", 120),
        User("Istvan Ștefania ", 110),
        User("User3", 105),
        User("User4", 90),
        User("User5", 85)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Utilizatorii cu cele mai multe răspunsuri corecte",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(users) { index, user ->
                UserRankingItem(position = index + 1, user = user)
            }
        }
    }
}

@Composable
fun UserRankingItem(position: Int, user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        //pozitie utilizator in clasament
        Text(
            text = "$position",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.width(30.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        //nume utilizator
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "${user.score} Răspunsuri corecte",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
