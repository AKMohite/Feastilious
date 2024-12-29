package com.mak.feastit.domain.repository

interface RecipeRepository {
    suspend fun refreshRecipe(id: Long, forceRefresh: Boolean = false)
    fun getRecipe(id: Long)
}
