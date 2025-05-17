package com.example.apptechchallengeoh.di.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptechchallengeoh.auth.domain.usecase.AuthUseCase
import com.example.apptechchallengeoh.di.signin.states.UiStateAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    // Estado para la UI (carga, éxito, error, etc.)
    private val _uiState = MutableStateFlow<UiStateAuth<String>>(UiStateAuth.Idle)
    val uiState: StateFlow<UiStateAuth<String>> = _uiState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated


    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        checkSession()
    }

    fun checkSession() {
        viewModelScope.launch {
            authUseCase.isAuthenticated().collect {
                _isAuthenticated.value = it
            }
        }
    }

    // Función de login
    fun login(email: String, password: String) {
        _uiState.value = UiStateAuth.Loading // Cambiar el estado a Loading
        viewModelScope.launch {
            // Llamada al UseCase para intentar hacer login
            val result = authUseCase.login(email, password)

            // Si el login es exitoso
            _uiState.value = if (result.isSuccess) {
                UiStateAuth.Success("Inicio de sesión exitoso") // Mensaje de éxito
            } else {
                UiStateAuth.Error(result.exceptionOrNull()?.message ?: "Error desconocido") // Mensaje de error
            }

            // Si el login es exitoso, marcar la sesión como autenticada
            if (result.isSuccess) {
                _isAuthenticated.value = true
            }
        }
    }

    // Función de registro de usuario
    fun register(email: String, password: String, confirmPassword: String) {
        // Verificar si las contraseñas coinciden
        if (password != confirmPassword) {
            _uiState.value = UiStateAuth.Error("Las contraseñas no coinciden") // Error si no coinciden
            return
        }

        _uiState.value = UiStateAuth.Loading // Cambiar a estado de carga
        viewModelScope.launch {
            val result = authUseCase.register(email, password)

            // Si el registro es exitoso
            _uiState.value = if (result.isSuccess) {
                UiStateAuth.Success("Registro exitoso") // Mensaje de éxito
            } else {
                UiStateAuth.Error(result.exceptionOrNull()?.message ?: "Error desconocido") // Mensaje de error
            }

            // Si el registro es exitoso, marcamos la sesión como autenticada
            if (result.isSuccess) {
                _isAuthenticated.value = true
            }
        }
    }

    // Setear error manualmente
    fun setError(message: String) {
        _error.value = message
    }

    // Manejar errores específicos de login
    private fun handleLoginError(error: Throwable?) {
        when (error?.message) {
            "Usuario no encontrado" -> {
                _uiState.value = UiStateAuth.Error("El usuario no existe")
            }
            "Credenciales incorrectas" -> {
                _uiState.value = UiStateAuth.Error("Contraseña incorrecta")
            }
            else -> {
                _uiState.value = UiStateAuth.Error("Error desconocido")
            }
        }
    }
}