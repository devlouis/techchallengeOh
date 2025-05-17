package com.example.apptechchallengeoh.di.signin.states

open class UiStateAuth<out T> {
    object Loading : UiStateAuth<Nothing>()
    data class Success<T>(val data: T) : UiStateAuth<T>()
    data class Error(val message: String) : UiStateAuth<Nothing>()
    object Idle : UiStateAuth<Nothing>()
}