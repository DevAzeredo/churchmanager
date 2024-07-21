package pages

import PessoaService
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
import models.Pessoa
import ui.theme.Typography

@Composable
fun PeoplesPage(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val pessoas = remember { mutableStateListOf<Pessoa>() }

    LaunchedEffect(Unit) {
        val result = PessoaService.getPessoas()
        pessoas.addAll(result)
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
                            PessoaService.createPessoa(novaPessoa)
                            pessoas.add(novaPessoa)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PessoaList(pessoas)
                }
            }
        }
    )
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