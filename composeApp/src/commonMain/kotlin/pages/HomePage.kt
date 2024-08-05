package pages

import PessoaService
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import components.NavigationButton
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import models.Curso
import models.Pessoa
import services.CursoService

@Composable
fun HomePage(
    navController: NavHostController,
    registeredPeopleCount: Int,
    upcomingCourses: List<Curso>,
    birthdaysThisMonth: List<Pessoa>
) {
    Scaffold(topBar = {
        NavigationButton("Início", navController)
    }, content = { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Card(
                        modifier = Modifier.wrapContentWidth(
                        ).padding(8.dp).height(300.dp),
                    ) {
                        Column(
                            modifier = Modifier.wrapContentWidth().padding(26.dp),
                        ) {
                            Text(
                                text = "Pessoas Registradas: $registeredPeopleCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.wrapContentWidth(
                        ).padding(8.dp).height(300.dp),
                    ) {
                        Column(
                            modifier = Modifier.wrapContentWidth().padding(26.dp),
                        ) {
                            Text(
                                text = "Próximos Cursos",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(upcomingCourses) { course ->
                                    Text(
                                        text = "${course.nome} - ${course.data}", fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                    Card(
                        modifier = Modifier.wrapContentWidth(
                        ).padding(8.dp).height(300.dp),
                    ) {
                        Column(
                            modifier = Modifier.wrapContentWidth().padding(26.dp)
                        ) {
                            Text(
                                text = "Aniversariantes do Mês",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(birthdaysThisMonth) { person ->
                                    Text(
                                        text = "${person.nome} - ${person.dataNascimento}",
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}


@Composable
fun HomePagePreview(navController: NavHostController) {
    val pessoas = remember { mutableStateListOf<Pessoa>() }
    val aniversariantes = remember { mutableStateListOf<Pessoa>() }
    val proximosCursos = remember { mutableStateListOf<Curso>() }

    LaunchedEffect(Unit) {
        PessoaService.getPessoas().onSuccess {
            pessoas.addAll(it)
        }.onFailure { //faz nada
        }
        CursoService.proximosCursos(5).onSuccess {
            proximosCursos.addAll(it)
        }.onFailure { //faz nada
        }
        val mesAtual =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).month.number
        PessoaService.getAniversariantesDoMe(mesAtual).onSuccess {
            aniversariantes.addAll(it)
        }.onFailure {
            // faz nada
        }
    }
    HomePage(
        navController = navController,
        registeredPeopleCount = pessoas.size,
        upcomingCourses = proximosCursos,
        birthdaysThisMonth = aniversariantes
    )
}