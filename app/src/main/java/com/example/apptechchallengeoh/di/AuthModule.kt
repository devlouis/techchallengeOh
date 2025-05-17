package com.example.apptechchallengeoh.di

import android.app.Application
import android.content.Context
import com.example.apptechchallengeoh.auth.data.repository.AuthRepository
import com.example.apptechchallengeoh.auth.domain.model.AuthUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, context: Context): AuthRepository {
        return AuthRepository(firebaseAuth, context)
    }

    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(authRepository)
    }
}