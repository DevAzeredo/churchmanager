import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import pages.CoursesPage
import pages.HomePage
import pages.PeoplesPage
import ui.theme.MyApplicationTheme

sealed class Screen {
    data object Home : Screen()
    data object Courses : Screen()
    data object Peoples : Screen()
}

@Composable
@Preview
fun App() {
    MyApplicationTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        when (currentScreen) {
            is Screen.Home -> HomePage(
                navigateToCourses = { currentScreen = Screen.Courses },
                navigateToPeoples = { currentScreen = Screen.Peoples })

            is Screen.Courses -> CoursesPage()
            is Screen.Peoples -> PeoplesPage()
        }
    }
}