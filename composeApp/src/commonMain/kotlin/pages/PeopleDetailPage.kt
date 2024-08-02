package pages

import PessoaService
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import models.Curso
import models.Pessoa
import services.CursoService


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleDetailPage(navController: NavHostController, pessoa: Pessoa) {
    val scope = rememberCoroutineScope()
    var cursosVinculados = remember { mutableStateListOf<Curso>() }
    val cursosDisponiveis = remember { mutableStateListOf<Curso>() }
    var cursoSelecionado by remember { mutableStateOf<Curso?>(null) }
    var showDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        CursoService.getCursos()
            .onSuccess {
                cursosDisponiveis.addAll(it)
            }
            .onFailure {
                // faz nada
            }
        CursoService.getCursosByPessoaId(pessoa.id).onSuccess {
            cursosVinculados.addAll(it)
        }
            .onFailure {
                // faz nada
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Pessoa") },
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(text = "Nome: ${pessoa.nome}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tel/Cel: ${pessoa.telefone}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Email: ${pessoa.email}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Profissão: ${pessoa.profissao}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Endereço: ${pessoa.endereco}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data de Nascimento: ${pessoa.dataNascimento}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { showDropdown = !showDropdown }) {
                        Text("Vincular Curso")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (showDropdown) {
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            cursosDisponiveis.forEach { curso ->
                                DropdownMenuItem(text = { Text(curso.nome) }, onClick = {
                                    cursoSelecionado =
                                        curso.let { Curso(it.id, it.nome, it.descricao, it.data) }
                                    showDropdown = false

                                })
                            }
                        }
                    }

                    cursoSelecionado?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            scope.launch {
                                PessoaService.addCursoToPessoa(pessoa, cursoSelecionado!!.id)
                                    .onSuccess {
                                        cursosVinculados.add(cursoSelecionado!!)
                                        cursoSelecionado = null
                                    }
                                    .onFailure {
                                        cursoSelecionado = null
                                    }

                            }
                        }) {
                            Text("Adicionar ${it.nome}")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cursos Vinculados:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(cursosVinculados) { curso ->
                            Text(text = curso.nome, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}