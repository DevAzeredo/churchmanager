package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.Curso
import services.CursoService
import ui.theme.Typography

@Composable
fun CoursesPage() {
    val scope = rememberCoroutineScope()
    val cursos = remember { mutableStateListOf<Curso>() }
    var novoCursoNome by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val result = CursoService.getCursos()
        cursos.addAll(result)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cursos") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
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
                        value = novoCursoNome,
                        onValueChange = { novoCursoNome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    var novoCursoDescricao = ""
                    OutlinedTextField(
                        value = novoCursoDescricao,
                        onValueChange = { novoCursoDescricao = it },
                        label = { Text("Descricao ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val novoCurso = Curso(
                                0,
                                novoCursoNome,
                                novoCursoDescricao,
                                "2023-12-18"
                            ) // Replace with actual date
                            scope.launch {
                                CursoService.createCurso(novoCurso)
                                if (novoCurso.id > 0) {
                                    cursos.add(novoCurso)
                                    novoCursoNome = ""
                                    novoCursoDescricao = ""
                                }
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
                                        text = "Nome: ${curso.nome}",
                                        style = Typography.body1
                                    )
                                    Text(
                                        text = "Descrição: ${curso.descricao}",
                                        style = Typography.body2
                                    )
                                    Text(
                                        text = "Criado em: ${curso.data}",
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