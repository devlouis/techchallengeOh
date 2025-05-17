package com.example.apptechchallengeoh.home.data.network

import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import retrofit2.http.GET

interface ApiService {

    @GET("categories")
    suspend fun getCategories(): List<CategoryModel>  // Suponiendo que la API devuelve una lista de categorías
}