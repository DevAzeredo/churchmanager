package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.NavigationButton
import kotlinx.coroutines.launch
import models.Curso
import services.CursoService
import ui.theme.Typography

@Composable
fun CoursesPage(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val cursos = remember { mutableStateListOf<Curso>() }
    var novoNome by remember { mutableStateOf("") }
    var novaDescricao by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val result = CursoService.getCursos()
        cursos.addAll(result)
    }
    Scaffold(
        topBar = {
            NavigationButton("Cursos", navController)
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Form to add a new course
                    OutlinedTextField(
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = novaDescricao,
                        onValueChange = { novaDescricao = it },
                        label = { Text("Descricao ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val novoCurso = Curso(
                                0,
                                novoNome,
                                novaDescricao,
                                "2023-12-18"
                            ) // Replace with actual date
                            scope.launch {
                                CursoService.createCurso(novoCurso)

                                    cursos.add(novoCurso)
                                    novoNome = ""
                                    novaDescricao = ""

                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Curso")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // List of courses
                    LazyColumn {
                        items(cursos) { curso ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = curso.nome,
                                        style = Typography.body1
                                    )
                                    Text(
                                        text = curso.descricao,
                                        style = Typography.body2
                                    )
                                    Text(
                                        text = curso.data,
                                        style = Typography.caption
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}