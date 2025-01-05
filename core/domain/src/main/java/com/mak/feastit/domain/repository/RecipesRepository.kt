package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    suspend fun refreshRecipes(request: SyncType, page: Int, forceRefresh: Boolean = false)
    fun getRecipes(request: SyncType, page: Int): Flow<List<Recipe>>
    fun observeFavoriteRecipes(): Flow<List<Recipe>>
}