package pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import models.Pessoa
import ui.theme.Typography

@Composable
fun PeopleDetailPage(navController: NavHostController, pessoa: Pessoa) {
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
        content = {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(text = "Nome: ${pessoa.nome}", style = Typography.h6)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tel/Cel: ${pessoa.telefone}", style = Typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Email: ${pessoa.email}", style = Typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Profissão: ${pessoa.profissao}", style = Typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Endereço: ${pessoa.endereco}", style = Typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data de Nascimento: ${pessoa.dataNascimento}",
                        style = Typography.body1
                    )
                    // Adicione mais detalhes conforme necessário
                }
            }
        }
    )
}