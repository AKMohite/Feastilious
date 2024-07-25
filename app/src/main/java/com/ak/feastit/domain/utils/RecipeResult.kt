package com.ak.feastit.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * [Reference](https://github.com/fethij/Rijksmuseum/blob/main/core/common/src/commonMain/kotlin/com/tewelde/rijksmuseum/core/common/Result.kt)
 *
 * Extension function to convert a Flow<T> to a Flow<Result<T>>
 */
fun <T> Flow<T>.asResult(): Flow<RecipeResult<T>> {
    return this
        .map<T, RecipeResult<T>> {
            RecipeResult.success(it)
        }.catch {
            emit(RecipeResult.error(it.message ?: "Cannot get result. Please try again later")) }
}

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