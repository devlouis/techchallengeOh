package com.example.apptechchallengeoh.auth.domain.usecase

import com.example.apptechchallengeoh.auth.data.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                Result.success("Login exitoso") // O cualquier otro mensaje adecuado
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            when (e.message) {
                "The password is invalid or the user does not have a password." -> {
                    Result.failure(Exception("Credenciales incorrectas"))
                }
                "There is no user record corresponding to this identifier." -> {
                    Result.failure(Exception("Usuario no encontrado"))
                }
                else -> {
                    Result.failure(Exception("Error desconocido"))
                }
            }
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return authRepository.register(email, password)
    }

    suspend fun isAuthenticated(): Flow<Boolean> {
        return authRepository.isAuthenticated()
    }
}