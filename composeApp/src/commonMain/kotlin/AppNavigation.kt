import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pages.CoursesPage
import pages.HomePage
import pages.PeoplesPage

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController) }
        composable("courses") { CoursesPage(navController) }
        composable("peoples") { PeoplesPage(navController) }
    }
}