package pages

import PessoaService
import androidx.compose.foundation.clickable
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(46.dp))
                    Button(
                        onClick = { showForm = !showForm },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showForm) "Cancelar" else "Adicionar Pessoa")
                    }

                    if (showForm) {
                        AddPessoaForm(
                            onAddPessoa = { novaPessoa ->
                                scope.launch {
                                    pessoas.add(novaPessoa)
                                    filteredPessoas.add(novaPessoa)
                                }
                                showForm = false
                            },
                            onClearClick = {
                                // Lógica para limpar os campos, se necessário
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            filteredPessoas.clear()
                            filteredPessoas.addAll(pessoas.filter { pessoa ->
                                pessoa.nome.contains(searchQuery, ignoreCase = true)
                            })
                        },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PessoaList(navController, filteredPessoas)

                    if (showNotification) {
                        NotificationCard(errorMessage = errorMessage) {
                            showNotification = false
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AddPessoaForm(onAddPessoa: (Pessoa) -> Unit, onClearClick: () -> Unit) {
    var novoNome by remember { mutableStateOf("") }
    var novoTelCel by remember { mutableStateOf("") }
    var novoEndereco by remember { mutableStateOf("") }
    var novoProfissao by remember { mutableStateOf("") }
    var novoEmail by remember { mutableStateOf("") }
    var novoDataNascimento by remember { mutableStateOf("") }

    Column {
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedTextField(
            value = novoNome,
            onValueChange = { novoNome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoTelCel,
            onValueChange = { novoTelCel = it },
            label = { Text("Tel/Cel") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoEmail,
            onValueChange = { novoEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoProfissao,
            onValueChange = { novoProfissao = it },
            label = { Text("Profissão") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoEndereco,
            onValueChange = { novoEndereco = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoDataNascimento,
            onValueChange = { novoDataNascimento = it },
            label = { Text("Data Nascimento") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val novaPessoa = Pessoa(
                        0,
                        novoNome,
                        novoDataNascimento,
                        novoEndereco,
                        novoTelCel,
                        novoEmail,
                        novoProfissao
                    )
                    onAddPessoa(novaPessoa)
                    novoNome = ""
                    novoDataNascimento = ""
                    novoEndereco = ""
                    novoTelCel = ""
                    novoEmail = ""
                    novoProfissao = ""
                },
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
fun PessoaList(navController: NavHostController, pessoas: List<Pessoa>) {
    LazyColumn {
        items(pessoas) { pessoa ->
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
                    Text(text = pessoa.nome, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Tel/Cel: ${pessoa.telefone}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Email: ${pessoa.email}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Profissão: ${pessoa.profissao}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Endereço: ${pessoa.endereco}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Data de Nascimento: ${pessoa.dataNascimento}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
