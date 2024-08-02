package components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController


@Composable
fun NavigationButton(title: String, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.inversePrimary,
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    navController.navigate("home")
                }) {
                    Text("Home")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    navController.navigate("courses")
                }) {
                    Text("Curso")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    navController.navigate("peoples")
                }) {
                    Text("Pessoas")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    navController.navigate("groups")
                }) {
                    Text("Grupos")
                }
            }
        }
    )
}