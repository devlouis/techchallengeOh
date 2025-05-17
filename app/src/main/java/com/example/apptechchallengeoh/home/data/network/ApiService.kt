package com.example.apptechchallengeoh.home.data.network

import retrofit2.http.GET

interface ApiService {

    @GET("categories")
    suspend fun getCategories(): List<String>  // Suponiendo que la API devuelve una lista de categorías
}