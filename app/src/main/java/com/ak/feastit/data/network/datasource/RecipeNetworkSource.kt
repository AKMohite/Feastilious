package com.ak.feastit.data.network.datasource

import com.ak.feastit.data.local.relations.RecipeDetailEntity

interface RecipeNetworkSource {
    suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDetailEntity>
    suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDetailEntity>
}