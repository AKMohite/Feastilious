package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.RecipeDetail
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun refreshRecipe(id: Long, forceRefresh: Boolean = false)
    fun observerRecipe(id: Long): Flow<RecipeDetail>
    suspend fun refreshAnalyzedInstruction(id: Long, forceRefresh: Boolean)
    suspend fun refreshSimilarRecipes(id: Long, forceRefresh: Boolean)
}
