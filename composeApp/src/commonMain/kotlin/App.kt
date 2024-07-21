import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.MyApplicationTheme


@Composable
@Preview
fun App() {
    MyApplicationTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}