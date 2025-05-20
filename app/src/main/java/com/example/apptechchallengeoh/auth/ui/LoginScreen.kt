package com.example.apptechchallengeoh.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apptechchallengeoh.R
import com.example.apptechchallengeoh.auth.states.UiStateAuth
import com.example.apptechchallengeoh.auth.viewmodel.AuthViewModel
import com.example.apptechchallengeoh.ui.ViewLoading


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isPasswordVisible by authViewModel.isPasswordVisible.collectAsState()

    // Obtener el estado del login
    val uiState by authViewModel.uiState.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    // Si el usuario ya está autenticado, redirigir al home
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate("homeScreen") {
                popUpTo("loginScreen") { inclusive = true } // Evitar que el usuario vuelva a la pantalla de login
            }
        }
    }

    // Mostrar el estado de carga, error o éxito
    when (uiState) {
        is UiStateAuth.Loading -> {
            ViewLoading()
        }
        is UiStateAuth.Error -> {
            Text(
                text = (uiState as UiStateAuth.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        is UiStateAuth.Success -> {
        }
    }

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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        val image =
                            if (isPasswordVisible) painterResource(id = R.drawable.ic_visibility_password) else painterResource(
                                id = R.drawable.visibility_off_password
                            )
                        IconButton(onClick = { authViewModel.togglePasswordVisibility() }) {
                            Icon(painter = image, contentDescription = null)
                        }
                    }

                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        authViewModel.login(email, password)
                        keyboardController?.hide()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para ir al registro
                TextButton(onClick = { navController.navigate("registerScreen") }) {
                    Text("¿No tienes cuenta? Regístrate")
                }

                // Mostrar mensaje de error si el estado es 'Error'
                if (uiState is UiStateAuth.Error) {
                    val errorMessage = (uiState as UiStateAuth.Error).message
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}