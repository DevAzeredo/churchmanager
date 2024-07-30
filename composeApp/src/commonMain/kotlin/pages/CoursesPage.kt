package pages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import components.NavigationButton
import components.NotificationCard
import kotlinx.coroutines.launch
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
    val datePickerState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input)
    var showForm by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredCursos = remember { mutableStateListOf<Curso>() }
    LaunchedEffect(Unit) {
        CursoService.getCursos()
            .onSuccess {
                cursos.addAll(it)
            }
            .onFailure {
                errorMessage = it.message ?: "Unknown error"
                showNotification = true
            }
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
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(46.dp))
                    Button(
                        onClick = { showForm = !showForm },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showForm) "Cancelar" else "Adicionar Curso")
                    }

                    if (showForm) {
                        CourseForm(
                            novoNome = novoNome,
                            onNomeChange = { novoNome = it },
                            novaDescricao = novaDescricao,
                            onDescricaoChange = { novaDescricao = it },
                            datePickerState = datePickerState,
                            onAddClick = {
                                val novoCurso = Curso(
                                    0,
                                    novoNome,
                                    novaDescricao,
                                    "${datePickerState.selectedStartDateMillis}",
                                    "${datePickerState.selectedEndDateMillis}"
                                )
                                scope.launch {
                                    CursoService.createCurso(novoCurso)
                                        .onSuccess {
                                            cursos.add(it)
                                        }
                                        .onFailure {
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

                    Spacer(modifier = Modifier.height(16.dp))
                    if (showNotification) {
                        NotificationCard(errorMessage = errorMessage) {
                            showNotification = false
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            filteredCursos.clear()
                            filteredCursos.addAll(cursos.filter { curso ->
                                curso.nome.contains(searchQuery, ignoreCase = true)
                            })
                        },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CourseList(filteredCursos)
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
    datePickerState: DateRangePickerState,
    onAddClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
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
        DateRangePicker(
            title = {
                Text(
                    text = "Selecione o Período de início e final do curso",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            },
            state = datePickerState,
            modifier = Modifier.fillMaxWidth().height(
                if (datePickerState.displayMode == DisplayMode.Picker) {
                    500.dp
                } else {
                    300.dp
                }
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onAddClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Adicionar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onClearClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
    }
}

@Composable
fun CourseList(cursos: List<Curso>) {
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
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = curso.descricao,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = curso.dataInicio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}