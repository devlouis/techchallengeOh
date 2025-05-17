package com.example.apptechchallengeoh.network

import com.example.apptechchallengeoh.home.data.network.ApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Proveedor de Moshi
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    // Proveedor de Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://53549cf7-784e-438f-aeae-11c382861c6a.mock.pstmn.io/") // URL base de la API
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Usamos Moshi como convertidor
            .build()
    }

    // Proveedor de ApiService
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

