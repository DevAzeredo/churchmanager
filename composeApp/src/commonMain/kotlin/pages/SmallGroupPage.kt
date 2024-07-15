package pages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import models.PequenoGrupo

@Composable
fun SmallGroupPage() {
    val scope = rememberCoroutineScope()
    val pequenosGrupos = remember { mutableStateListOf<PequenoGrupo>() }

    scope.launch {
        val result = PequenoGrupoService.getPequenosGrupos()
        pequenosGrupos.addAll(result)
    }

    LazyColumn {
        items(pequenosGrupos) { pequenoGrupo ->
            Text(pequenoGrupo.nome)
        }
    }
}