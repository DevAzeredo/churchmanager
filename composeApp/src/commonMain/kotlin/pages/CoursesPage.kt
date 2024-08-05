package pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import components.DateInputComponent
import components.NavigationButton
import components.NotificationCard
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.Curso
import services.CursoService


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesPage(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val cursos = remember { mutableStateListOf<Curso>() }
    var novoNome by remember { mutableStateOf("") }
    var novaDescricao by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredCursos = remember { mutableStateListOf<Curso>() }
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val lazyListSate = rememberLazyListState()

    LaunchedEffect(Unit) {
        CursoService.getCursos().onSuccess {
            cursos.addAll(it)
            filteredCursos.addAll(it)
        }.onFailure {
            errorMessage = "o erro e: ${it.message}" ?: "Unknown error"
            showNotification = true
        }
    }

    Scaffold(topBar = {
        NavigationButton("Cursos", navController)
    }, content = {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(46.dp)) }
                item {
                    Button(
                        onClick = { showForm = !showForm }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showForm) "Cancelar" else "Adicionar Curso")
                    }
                }

                item {
                    if (showForm) {
                        CourseForm(
                            novoNome = novoNome,
                            onNomeChange = { novoNome = it },
                            novaDescricao = novaDescricao,
                            onDescricaoChange = { novaDescricao = it },
                            state = state,
                            onAddClick = {
                                val novoCurso = Curso(
                                    0,
                                    novoNome,
                                    novaDescricao,
                                    state.selectedDateMillis?.let {
                                        val instant = Instant.fromEpochMilliseconds(it)
                                        val dateTime =
                                            instant.toLocalDateTime(TimeZone.currentSystemDefault())
                                        "${
                                            dateTime.dayOfMonth.toString().padStart(2, '0')
                                        }-${
                                            dateTime.monthNumber.toString().padStart(2, '0')
                                        }-${dateTime.year}"
                                    }!!
                                )
                                scope.launch {
                                    CursoService.createCurso(novoCurso).onSuccess {
                                        cursos.add(it)
                                        filteredCursos.add(it)
                                    }.onFailure {
                                        errorMessage = it.message ?: "Unknown error"
                                        showNotification = true
                                    }
                                    novoNome = ""
                                    novaDescricao = ""
                                    showForm = false
                                }
                            },
                            onClearClick = {
                                novoNome = ""
                                novaDescricao = ""
                            }
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            filteredCursos.clear()
                            filteredCursos.addAll(cursos.filter { curso ->
                                curso.nome.contains(searchQuery, ignoreCase = true)
                            })
                        },
                        label = { Text("Pesquisar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    if (showNotification) {
                        NotificationCard(errorMessage = errorMessage) {
                            showNotification = false
                        }
                    }
                }
                if (cursos.isEmpty()) {
                    item { Text(text = "Loading...", modifier = Modifier.padding(16.dp)) }
                } else {
                    items(cursos) { curso ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = curso.nome, style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = curso.descricao,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = curso.data, style = MaterialTheme.typography.bodyMedium
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseForm(
    novoNome: String,
    onNomeChange: (String) -> Unit,
    novaDescricao: String,
    onDescricaoChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onClearClick: () -> Unit,
    state: DatePickerState
) {
    var openDatePicker by remember { mutableStateOf(false) }
    Column(
    ) {
        OutlinedTextField(
            value = novoNome,
            onValueChange = onNomeChange,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novaDescricao,
            onValueChange = onDescricaoChange,
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        DateInputComponent(state)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onAddClick, modifier = Modifier.weight(1f)
            ) {
                Text("Adicionar")
            }
            Button(
                onClick = onClearClick, modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
    }
}
