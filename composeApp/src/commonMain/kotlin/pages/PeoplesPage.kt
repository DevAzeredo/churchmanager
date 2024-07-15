package pages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import models.Pessoa

@Composable
fun PeoplesPage() {
    val scope = rememberCoroutineScope()
    val pessoas = remember { mutableStateListOf<Pessoa>() }

    scope.launch {
        val result = PessoaService.getPessoas()
        pessoas.addAll(result)
    }

    LazyColumn {
        items(pessoas) { pessoa ->
            Text(pessoa.nome)
        }
    }
}

