package pages

import PessoaService
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import components.FormPessoaComponent
import components.NavigationButton
import components.NotificationCard
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Pessoa

@Composable
fun PeoplesPage(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val pessoas = remember { mutableStateListOf<Pessoa>() }
    val filteredPessoas = remember { mutableStateListOf<Pessoa>() }
    var errorMessage by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        PessoaService.getPessoas()
            .onSuccess {
                pessoas.addAll(it)
                filteredPessoas.addAll(it)
            }
            .onFailure {
                errorMessage = it.message ?: "Unknown error"
                showNotification = true
            }
    }
    Scaffold(
        topBar = {
            NavigationButton("Pessoas", navController)
        },
        content = {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(46.dp)) }
                    item {
                        Button(
                            onClick = { showForm = !showForm },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (showForm) "Cancelar" else "Adicionar Pessoa")
                        }
                    }

                    if (showForm) {
                        item {
                            FormPessoaComponent(
                                onAddPessoa = { novaPessoa ->
                                    scope.launch {
                                        PessoaService.createPessoa(novaPessoa).onSuccess {
                                            pessoas.add(novaPessoa)
                                            filteredPessoas.add(novaPessoa)
                                        }.onFailure {
// faz nada por enquanto
                                        }
                                    }
                                    showForm = false
                                },
                                onClearClick = {
                                    // Lógica para limpar os campos, se necessário
                                }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                filteredPessoas.clear()
                                filteredPessoas.addAll(pessoas.filter { pessoa ->
                                    pessoa.nome.contains(searchQuery, ignoreCase = true)
                                })
                            },
                            label = { Text("Pesquisar") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    items(filteredPessoas) { pessoa ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    val pessoaJson = Json.encodeToString(pessoa)
                                    navController.navigate("pessoaDetail/$pessoaJson")
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = pessoa.nome,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    item {
                        if (showNotification) {
                            NotificationCard(errorMessage = errorMessage) {
                                showNotification = false
                            }
                        }
                    }
                }
            }
        }
    )
}

