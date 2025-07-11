// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

const val IMG_INGREDIENT_BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
const val QUERY_SEARCH = "query"
const val QUERY_TYPE = "type"

/**
 * [Reference](https://github.com/fethij/Rijksmuseum/blob/main/core/common/src/commonMain/kotlin/com/tewelde/rijksmuseum/core/common/Result.kt)
 *
 * Extension function to convert a Flow<T> to a Flow<Result<T>>
 */
fun <T> Flow<T>.asResult(): Flow<RecipeResult<T>> = this
  .map<T, RecipeResult<T>> {
    com.mak.feastit.domain.model.RecipeResult
      .success(it)
  }.catch {
    emit(
      com.mak.feastit.domain.model.RecipeResult.error(
        it.message ?: "Cannot get result. Please try again later",
      ),
    )
  }

data class RecipeResult<out T>(
  val data: T? = null,
  val error: String? = null,
  val loading: Boolean = false,
) {
  companion object {
    fun <T> success(data: T): RecipeResult<T> = RecipeResult(
      data = data,
    )

    fun <T> error(message: String): RecipeResult<T> = RecipeResult(
      error = message,
    )

    fun <T> loading(): RecipeResult<T> = RecipeResult(loading = true)
  }
}
