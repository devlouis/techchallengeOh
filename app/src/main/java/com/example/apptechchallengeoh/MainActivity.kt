package com.example.apptechchallengeoh

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptechchallengeoh.auth.ui.LoginScreen
import com.example.apptechchallengeoh.auth.ui.RegisterScreen
import com.example.apptechchallengeoh.auth.viewmodel.AuthViewModel
import com.example.apptechchallengeoh.ui.theme.AppTechChallengeOhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Accedemos al ViewModel utilizando el delegado `by viewModels()`
        val authViewModel: AuthViewModel by viewModels()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*AppTechChallengeOhTheme {
                // Crear un NavController para la navegación
                val navController = rememberNavController()

                // Llamar a MainNavigation y pasarle el navController
                MainNavigation(navController = navController)
            }*/
            setContent {
                val navController = rememberNavController() // Inicializa NavController aquí
                val isAuthenticated by authViewModel.isAuthenticated.collectAsState() // Accede al estado de autenticación

                // Usamos LaunchedEffect para manejar la navegación basada en la autenticación
                LaunchedEffect(isAuthenticated) {
                    if (isAuthenticated) {
                        navController.navigate("homeScreen") {
                            // Eliminar loginScreen de la pila para que no se pueda volver
                            popUpTo("loginScreen") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate("loginScreen")
                    }
                }

                // Configuramos el NavHost con el NavController y el gráfico de navegación
                NavHost(navController = navController, startDestination = "loginScreen") {
                    composable("loginScreen") {
                        LoginScreen(navController = navController)
                    }
                    composable("registerScreen") {
                        RegisterScreen(navController = navController)
                    }
                    composable("homeScreen") {
                        HomeScreen()
                    }
                }
            }
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