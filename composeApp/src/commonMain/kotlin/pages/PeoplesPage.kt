package pages

import PessoaService
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.NavigationButton
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Pessoa
import ui.theme.Typography

@Composable
fun PeoplesPage(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val pessoas = remember { mutableStateListOf<Pessoa>() }
    val filteredPessoas = remember { mutableStateListOf<Pessoa>() }
    var errorMessage by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

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
                    AddPessoaForm { novaPessoa ->
                        scope.launch {
                            pessoas.add(novaPessoa)
                            filteredPessoas.add(novaPessoa)
                        }
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
                    Text(text = pessoa.nome, style = Typography.body1)
                    Text(text = "Tel/Cel: ${pessoa.telefone}", style = Typography.body2)
                    Text(text = "Email: ${pessoa.email}", style = Typography.body2)
                    Text(text = "Profissão: ${pessoa.profissao}", style = Typography.body2)
                    Text(text = "Endereço: ${pessoa.endereco}", style = Typography.body2)
                    Text(
                        text = "Data de Nascimento: ${pessoa.dataNascimento}",
                        style = Typography.body2
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(errorMessage: String?, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notification Icon",
                tint = MaterialTheme.colors.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Erro",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = errorMessage ?: "Unknown error",
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Se o erro persistir, entre em contato com o suporte técnico.",
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Notification"
                )
            }
        }
    }
}


@Composable
fun AddPessoaForm(onAddPessoa: (Pessoa) -> Unit) {
    var novoNome by remember { mutableStateOf("") }
    var novoTelCel by remember { mutableStateOf("") }
    var novoEndereco by remember { mutableStateOf("") }
    var novoProfissao by remember { mutableStateOf("") }
    var novoEmail by remember { mutableStateOf("") }
    var novoDataNascimento by remember { mutableStateOf("") }
    Column {
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
            label = { Text("Profissao") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoEndereco,
            onValueChange = { novoEndereco = it },
            label = { Text("Endereco") },
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Pessoa")
        }
    }
}

@Composable
fun PessoaList(pessoas: List<Pessoa>) {
    LazyColumn {
        items(pessoas) { pessoa ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = pessoa.nome,
                        style = Typography.body1
                    )
                    Text(
                        text = "Tel/Cel: ${pessoa.telefone}",
                        style = Typography.body2
                    )
                    Text(
                        text = "Email: ${pessoa.email}",
                        style = Typography.body2
                    )
                    Text(
                        text = "Profissao: ${pessoa.profissao}",
                        style = Typography.body2
                    )
                    Text(
                        text = "Endereco: ${pessoa.endereco}",
                        style = Typography.body2
                    )
                    Text(
                        text = "Data Nascimento: ${pessoa.dataNascimento}",
                        style = Typography.body2
                    )
                }
            }
        }
    }
}