
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import models.Pessoa
import pages.CoursesPage
import pages.HomePage
import pages.PeopleDetailPage
import pages.PeoplesPage

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController) }
        composable("courses") { CoursesPage(navController) }
        composable("peoples") { PeoplesPage(navController) }
        composable("peopleList") { PeoplesPage(navController) }
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
    }
}