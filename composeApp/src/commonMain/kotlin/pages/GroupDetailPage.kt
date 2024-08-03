package pages

import PessoaService
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import kotlinx.coroutines.launch
import models.Grupo
import models.Pessoa


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailPage(
    navController: NavHostController, grupo: Grupo
) {
    val scope = rememberCoroutineScope()
    var novoNome by remember { mutableStateOf(grupo.nome) }
    var novaDescricao by remember { mutableStateOf(grupo.descricao) }
    val pessoasVinculadas = remember { mutableStateListOf<Pessoa>() }
    val pessoasDisponiveis = remember { mutableStateListOf<Pessoa>() }
    val pessoasFiltradas = remember { mutableStateListOf<Pessoa>() }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        PessoaService.getPessoasByGrupoId(grupo.id).onSuccess {
            pessoasVinculadas.addAll(it)
        }.onFailure { //faz nada
        }
        PessoaService.getPessoas()
            .onSuccess {
                pessoasDisponiveis.addAll(it)
                pessoasFiltradas.addAll(it)
            }
            .onFailure {
                // faz nada
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Grupo") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    item {
                        OutlinedTextField(
                            value = novoNome,
                            onValueChange = { novoNome = it },
                            label = { Text("Nome") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = novaDescricao,
                            onValueChange = { novaDescricao = it },
                            label = { Text("Descricao") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                pessoasFiltradas.clear()
                                pessoasFiltradas.addAll(pessoasDisponiveis.filter { pessoa ->
                                    pessoa.nome.contains(searchQuery, ignoreCase = true)
                                })
                            },
                            label = { Text("Pesquisar") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(pessoasFiltradas) { pessoa ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Checkbox(
                                checked = pessoasVinculadas.contains(pessoa),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        scope.launch {
                                            PessoaService.addGrupoToPessoa(pessoa, grupo.id)
                                                .onSuccess {
                                                    pessoasVinculadas.add(pessoa)
                                                }
                                                .onFailure {
                                                    // faz nada
                                                }
                                        }
                                    } else {
                                        scope.launch {
                                            PessoaService.removeGrupoToPessoa(pessoa, grupo.id)
                                                .onSuccess {
                                                    pessoasVinculadas.remove(pessoa)
                                                }
                                                .onFailure {
                                                    // faz nada
                                                }
                                        }
                                    }
                                    pessoasFiltradas.sortByDescending {
                                        pessoasVinculadas.contains(
                                            it
                                        )
                                    }
                                }
                            )
                            Text(pessoa.nome, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}