package com.ak.feastit.data.local.datasource

import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.domain.recipelist.Recipe

interface RecipeLocalDataSource {
    suspend fun saveNetworkRecipes(recipeDetails: List<RecipeDetailEntity>): Boolean
    suspend fun searchLocalRecipes(searchQuery: String): List<Recipe>
    suspend fun searchLocalRecipesByMealType(mealType: String): List<Recipe>
}