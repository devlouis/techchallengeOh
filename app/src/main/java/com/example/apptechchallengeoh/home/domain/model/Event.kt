package com.example.apptechchallengeoh.home.domain.model

import com.google.firebase.Timestamp
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Event(
    val id: String? = "",
    val name: String? = "",
    val description: String? = "",
    val date: Timestamp? = null, // Timestamp de Firestore para fecha y hora
    val category: String? = "",
    val priority: String? = "",
    val isCompleted: Boolean? = false
)