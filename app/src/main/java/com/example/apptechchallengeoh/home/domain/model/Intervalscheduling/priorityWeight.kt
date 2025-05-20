package com.example.apptechchallengeoh.home.domain.model.Intervalscheduling

fun priorityWeight(priority: String?): Int = when (priority) {
    "High", "Alta"    -> 3
    "Medium", "Media" -> 2
    "Low", "Baja"     -> 1
    else                 -> 0
}