package com.example.apptechchallengeoh.auth.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Usamos DataStore para guardar la sesión
val Context.dataStore by preferencesDataStore(name = "user_prefs")
val USER_LOGGED_IN = stringPreferencesKey("user_logged_in")

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context
) {

    // Iniciar sesión
    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                saveSession("true")
                Result.success(result.user?.uid ?: "")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isAuthenticated(): Flow<Boolean> {
        return flow {
            emit(firebaseAuth.currentUser != null)
        }
    }

    // Guardar sesión
    private suspend fun saveSession(value: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = value
        }
    }
}