package com.ak.feastit.domain.datasource

import com.ak.feastit.data.local.relations.RecipeDetailEntity

interface IRecipeNetworkSource {
    suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDetailEntity>
    suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDetailEntity>
}