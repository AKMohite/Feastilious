package com.mak.feastit.database.datasource

import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity

interface IRecipeLocalDataSource {
    suspend fun saveNetworkRecipes(recipeDetails: List<RecipeDetailEntity>): Boolean
    suspend fun searchLocalRecipes(searchQuery: String): List<RecipeEntity>
    suspend fun getFavRecipes(searchQuery: String): List<RecipeEntity>
    suspend fun searchLocalRecipesByMealType(mealType: String): List<RecipeEntity>
    suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity
    suspend fun toggleFav(recipeId: Long, isFav: Boolean): Boolean
}