package com.ak.domain.utils

import kotlin.Exception

sealed class RecipeResult<out T> {
    data class Success<T>(val result: T): RecipeResult<T>()
    data class Error<T>(val error: Exception): RecipeResult<T>()
    object Loading: RecipeResult<Nothing>()
}