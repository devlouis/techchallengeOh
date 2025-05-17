package com.example.apptechchallengeoh.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apptechchallengeoh.di.signin.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val authViewModel: AuthViewModel = hiltViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //val authResult by authViewModel.authResult.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    /*LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate("homeScreen")
        }
    }*/

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Iniciar sesión") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo para email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para iniciar sesión
                Button(
                    onClick = { authViewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión")
                }

                // Mostrar error si el login falla
                /*if (authResult.isFailure) {
                    Text(
                        text = authResult.exceptionOrNull()?.message ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }*/

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para redirigir a registro
                TextButton(onClick = { navController.navigate("registerScreen") }) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    // Usamos rememberNavController() para simular un NavController en la vista previa
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}