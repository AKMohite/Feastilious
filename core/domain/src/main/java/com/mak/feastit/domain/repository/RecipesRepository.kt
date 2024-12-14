package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.Recipe

interface RecipesRepository {
    suspend fun fetchRecipeFor(params: Int): List<Recipe>
}