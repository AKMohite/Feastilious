// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
  suspend fun refreshRecipes(
    request: SyncType,
    page: Int,
    forceRefresh: Boolean = false,
  )

  fun getRecipes(
    request: SyncType,
    page: Int,
  ): Flow<List<Recipe>>

  fun observePaginatedRecipes(
    request: SyncType,
    pagingConfig: PagingConfig,
  ): Flow<PagingData<Recipe>>

  fun observeFavoriteRecipes(): Flow<List<Recipe>>

  fun observeSearchSuggestions(query: String): Flow<List<Recipe>>

  fun observeQueryPaginatedRecipes(
    subType: String,
    pagingConfig: PagingConfig,
  ): Flow<PagingData<Recipe>>

  suspend fun searchRecipe(query: String): List<Recipe>
  suspend fun searchRecipeByImage(imagePath: String): List<Recipe>
}
