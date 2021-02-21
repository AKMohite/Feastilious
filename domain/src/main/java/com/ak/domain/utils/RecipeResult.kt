package com.ak.domain.utils

import java.lang.Exception

sealed class RecipeResult<out E, out V> {
    data class Success<out V>(val result: V): RecipeResult<Nothing, V>()
    data class Error<out E>(val error: E): RecipeResult<E, Nothing>()

    companion object Factory {
        inline fun <V> build(function: () -> V): RecipeResult<Exception, V> {
            return try {
                Success(function.invoke())
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}