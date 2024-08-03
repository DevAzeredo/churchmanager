
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import models.Grupo
import models.Pessoa
import pages.CoursesPage
import pages.GroupDetailPage
import pages.GroupsPage
import pages.HomePagePreview
import pages.PeopleDetailPage
import pages.PeoplesPage

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePagePreview(navController) }
        composable("courses") { CoursesPage(navController) }
        composable("peoples") { PeoplesPage(navController) }
        composable("peopleList") { PeoplesPage(navController) }
        composable("groups") { GroupsPage(navController) }
        composable(
            "pessoaDetail/{pessoa}",
            arguments = listOf(navArgument("pessoa") { type = NavType.StringType })
        ) { backStackEntry ->
            val pessoaJson = backStackEntry.arguments?.getString("pessoa")
            val pessoa = pessoaJson?.let { Json.decodeFromString<Pessoa>(it) }
            pessoa?.let {
                PeopleDetailPage(navController, it)
            }
        }
        composable(
            "grupoDetail/{grupo}",
            arguments = listOf(navArgument("grupo") { type = NavType.StringType })
        ) { backStackEntry ->
            val grupoJson = backStackEntry.arguments?.getString("grupo")
            val grupo = grupoJson?.let { Json.decodeFromString<Grupo>(it) }
            grupo?.let {
                GroupDetailPage(navController, it)
            }
        }

    }
}