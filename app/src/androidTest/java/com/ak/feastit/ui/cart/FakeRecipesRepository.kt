package com.ak.feastit.ui.cart

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeRecipesRepository : RecipesRepository {
  override suspend fun refreshRecipes(request: SyncType, page: Int, forceRefresh: Boolean) {}

  override fun getRecipes(request: SyncType, page: Int): Flow<List<Recipe>> = emptyFlow()

  override fun observePaginatedRecipes(request: SyncType, pagingConfig: PagingConfig): Flow<PagingData<Recipe>> = emptyFlow()

  override fun observeFavoriteRecipes(): Flow<List<Recipe>> = emptyFlow()

  override fun observeSearchSuggestions(query: String): Flow<List<Recipe>> = emptyFlow()

  override fun observeQueryPaginatedRecipes(subType: String, pagingConfig: PagingConfig): Flow<PagingData<Recipe>> = emptyFlow()

  override suspend fun searchRecipe(query: String): List<Recipe> = emptyList()

  override suspend fun searchRecipeByImage(imagePath: String): List<Recipe> = emptyList()
}
