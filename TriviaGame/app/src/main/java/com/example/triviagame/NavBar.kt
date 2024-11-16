import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviagame.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    onLogout: () -> Unit // logout callback
) {
    val items = listOf("Home", "User Ranking", "Profile")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Star, Icons.Filled.Person)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Star, Icons.Outlined.Person)

    CenterAlignedTopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = items[selectedItem],
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logomic),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(70.dp)
                    .padding(start = 15.dp)
            )
        },
        actions = {
            items.forEachIndexed { index, item ->
                IconButton(onClick = { onItemSelected(index) }) {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item,
                        tint = if (selectedItem == index) Color.Black else Color.Gray
                    )
                }
            }

            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.Gray
                )
            }
        }
    )
}
