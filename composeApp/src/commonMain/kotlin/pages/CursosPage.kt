package pages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun CursosPage() {
    val scope = rememberCoroutineScope()
    val cursos = remember { mutableStateListOf<Curso>() }

    scope.launch {
        val result = CursoService.getCursos()
        cursos.addAll(result)
    }

    LazyColumn {
        items(cursos) { curso ->
            Text(curso.nome)
        }
    }
}