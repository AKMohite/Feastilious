package com.ak.feastit.domain.utils

data class RecipeResult<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false
) {
    companion object{

        fun <T> success(
            data: T
        ): RecipeResult<T>{
            return RecipeResult(
                data = data
            )
        }

        fun <T> error(
            message: String
        ): RecipeResult<T>{
            return RecipeResult(
                error = message
            )
        }

        fun <T> loading(): RecipeResult<T> = RecipeResult(loading = true)
    }
}