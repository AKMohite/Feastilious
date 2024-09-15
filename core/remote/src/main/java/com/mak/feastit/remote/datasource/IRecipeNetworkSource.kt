package com.mak.feastit.remote.datasource

import com.mak.feastit.remote.dto.RecipeDTO

interface IRecipeNetworkSource {
    suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDTO>
    suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDTO>
}