package com.example.apptechchallengeoh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptechchallengeoh.auth.ui.LoginScreen
import com.example.apptechchallengeoh.auth.ui.RegisterScreen
import com.example.apptechchallengeoh.ui.theme.AppTechChallengeOhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTechChallengeOhTheme {
                // Crear un NavController para la navegación
                val navController = rememberNavController()

                // Llamar a MainNavigation y pasarle el navController
                MainNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") {
            LoginScreen(navController = navController)
        }
        composable("registerScreen") {
            RegisterScreen(navController = navController)
        }
        composable("homeScreen") {
           // HomeScreen(navController = navController)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTechChallengeOhTheme {
        Greeting("Android")
    }
}