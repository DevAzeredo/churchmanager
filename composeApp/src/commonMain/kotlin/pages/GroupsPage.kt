package pages

import GrupoService
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
import models.Grupo

@Composable
fun GroupsPage(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var showForm by remember { mutableStateOf(false) }
    val grupos = remember { mutableStateListOf<Grupo>() }
    val filteredGrupos = remember { mutableStateListOf<Grupo>() }
    var searchQuery by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        GrupoService.getGrupos()
            .onSuccess {
                grupos.addAll(it)
                filteredGrupos.addAll(it)
            }
            .onFailure {
                errorMessage = it.message ?: "Unknown error"
                showNotification = true
            }
    }

    Scaffold(topBar = {
        NavigationButton("Grupos", navController)
    },

        content = {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(46.dp)) }
                    item {
                        Button(
                            onClick = { showForm = !showForm }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (showForm) "Cancelar" else "Adicionar Grupo")
                        }
                    }
                    if (showForm) {
                        item {
                            AddGroupForm(
                                onAddGrupo = { grupo ->
                                    scope.launch {
                                        GrupoService.createGrupo(grupo).onSuccess {
                                            grupos.add(grupo)
                                            filteredGrupos.add(grupo)
                                        }.onFailure {
                                            // faz nada
                                        }
                                    }
                                    showForm = false
                                },
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        OutlinedTextField(value = searchQuery, onValueChange = {
                            searchQuery = it
                            filteredGrupos.clear()
                            filteredGrupos.addAll(grupos.filter { pessoa ->
                                pessoa.nome.contains(searchQuery, ignoreCase = true)
                            })
                        }, label = { Text("Pesquisar") }, modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(filteredGrupos) { grupo ->
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
                                val grupoJson = Json.encodeToString(grupo)
                                navController.navigate("grupodetail/$grupoJson")
                            }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = grupo.nome, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Descricao: ${grupo.descricao}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    if (showNotification) {
                        item {
                            NotificationCard(errorMessage = errorMessage) {
                                showNotification = false
                            }
                        }
                    }
                }
            }
        })
}


@Composable
fun AddGroupForm(onAddGrupo: (Grupo) -> Unit) {
    var novoNome by remember { mutableStateOf("") }
    var novaDescricao by remember { mutableStateOf("") }
    Column {
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedTextField(value = novoNome,
            onValueChange = { novoNome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = novaDescricao,
            onValueChange = { novaDescricao = it },
            label = { Text("Descricao") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val novoGrupo = Grupo(
                        0, novoNome, novaDescricao
                    )
                    onAddGrupo(novoGrupo)
                    novoNome = ""
                    novaDescricao = ""
                }, modifier = Modifier.weight(1f)
            ) {
                Text("Adicionar")
            }
            Spacer(modifier = Modifier.width(300.dp))
            Button(
                onClick = { novoNome = ""; novaDescricao = "" }, modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
    }
}