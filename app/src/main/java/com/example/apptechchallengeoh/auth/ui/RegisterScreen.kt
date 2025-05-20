package com.example.apptechchallengeoh.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.apptechchallengeoh.auth.states.UiStateAuth
import com.example.apptechchallengeoh.auth.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel() /*onRegister: (String, String) -> Unit*/) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Obtener el estado de registro (si el registro fue exitoso o si hubo un error)
    val uiState by authViewModel.uiState.collectAsState()
    // Escuchar si el usuario ya está autenticado, si lo está, redirigirlo a la pantalla principal
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.popBackStack() // Eliminar la pantalla actual de la pila
            navController.navigate("homeScreen") {
                // Aseguramos que la pantalla de login y registro no esté en la pila
                popUpTo("loginScreen") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Registrarse") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Volver a la pantalla de login
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para confirmar contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registrarse
                Button(
                    onClick = { authViewModel.register(email, password, confirmPassword) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar")
                }

                // Mostrar el indicador de carga si el estado es 'Loading'
                if (uiState is UiStateAuth.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
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

                // Si el registro es exitoso, mostrar un mensaje de éxito
                if (uiState is UiStateAuth.Success) {
                    Text(
                        text = (uiState as UiStateAuth.Success).data,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    )
}