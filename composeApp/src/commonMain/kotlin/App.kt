
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}