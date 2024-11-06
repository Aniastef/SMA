import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "User Ranking", "Profile")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Star, Icons.Filled.Person)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Star, Icons.Outlined.Person)

    TopAppBar(
        title = { Text(text = items[selectedItem], color = Color.DarkGray) },
        actions = {
            items.forEachIndexed { index, item ->
                IconButton(onClick = { selectedItem = index }) {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item,
                        tint = if (selectedItem == index) Color.Black else Color.Gray
                    )
                }
            }
        },
    )
}
