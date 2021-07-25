package com.ak.feastit.data.network.datasource

import com.ak.feastit.data.network.dto.RecipeDTO

interface IRecipeNetworkSource {
    suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDTO>
    suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDTO>
}