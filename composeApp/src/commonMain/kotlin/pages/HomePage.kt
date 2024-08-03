package pages

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
import models.Curso
import models.Pessoa

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
                        modifier = Modifier
                            .wrapContentWidth(
                            )
                            .padding(8.dp)
                            .height(200.dp),
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
                        modifier = Modifier
                            .wrapContentWidth(
                            )
                            .padding(8.dp)
                            .height(200.dp),
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
                        modifier = Modifier
                            .wrapContentWidth(
                            )
                            .padding(8.dp).height(200.dp),
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

val birthdaysThisMonth = listOf(
    Pessoa(
        1, "João Silva", "1990-07-10", "Rua A, 123", "123456789", "joao@example.com", "Engenheiro"
    ), Pessoa(
        2, "Maria Souza", "1985-07-22", "Rua B, 456", "987654321", "maria@example.com", "Professora"
    )
)

@Composable
fun HomePagePreview(navController: NavHostController) {
    var pessoas = remember { mutableStateListOf<Pessoa>() }
    LaunchedEffect(Unit) {
        PessoaService.getPessoas().onSuccess {
            pessoas.addAll(it)
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
    HomePage(
        navController = navController,
        registeredPeopleCount = pessoas.size,
        upcomingCourses = upcomingCourses,
        birthdaysThisMonth = birthdaysThisMonth
    )
}