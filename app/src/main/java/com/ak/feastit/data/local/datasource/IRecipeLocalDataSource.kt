package com.ak.feastit.data.local.datasource

import com.ak.feastit.data.local.RecipeEntity
import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.model.Recipe

interface IRecipeLocalDataSource {
    suspend fun saveNetworkRecipes(recipeDetails: List<RecipeDetailEntity>): Boolean
    suspend fun searchLocalRecipes(searchQuery: String): List<RecipeEntity>
    suspend fun getFavRecipes(searchQuery: String): List<RecipeEntity>
    suspend fun searchLocalRecipesByMealType(mealType: String): List<RecipeEntity>
    suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity
    suspend fun toggleFav(recipeId: Long, isFav: Boolean): Boolean
}