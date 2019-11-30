package com.differ.differcore.models

sealed class Either<out T : Any> {
    data class Success<out T : Any>(val value: T) : Either<T>()
    data class Error(val message: String, val cause: Exception? = null) : Either<Nothing>()
}