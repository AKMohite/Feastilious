package com.mak.feastit.domain.repository

interface RecipeRepository {
    suspend fun refreshRecipe(id: Long, forceRefresh: Boolean = false)
    fun getRecipe(id: Long)
    suspend fun refreshAnalyzedInstruction(id: Long, forceRefresh: Boolean)
    suspend fun refreshSimilarRecipes(id: Long, forceRefresh: Boolean)
}
