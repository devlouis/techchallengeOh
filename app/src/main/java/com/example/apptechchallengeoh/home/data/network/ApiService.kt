package com.example.apptechchallengeoh.home.data.network

import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.CategoryResponse
import retrofit2.http.GET

interface ApiService {

    @GET("categories")
    suspend fun getCategories(): CategoryResponse  // Suponiendo que la API devuelve una lista de categorías
}