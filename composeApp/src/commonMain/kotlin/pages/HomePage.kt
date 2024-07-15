package pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.MyApplicationTheme

@Composable
fun HomePage(navigateToCourses: () -> Unit, navigateToPeoples : () -> Unit) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Homepage") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle navigation icon press */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Bem-vindo!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Button(
                            onClick = navigateToCourses,
                            modifier = Modifier
                                .padding(16.dp)
                                .width(200.dp)
                                .height(50.dp),
                        ) {
                            Text("Cursos", fontSize = 18.sp)
                        }
                        Button(
                            onClick = navigateToPeoples,
                            modifier = Modifier
                                .padding(16.dp)
                                .width(200.dp)
                                .height(50.dp),
                        ) {
                            Text("Pessoas", fontSize = 18.sp)
                        }
                    }
                }
            }
        )

}
