package com.mak.feastit.remote.datasource

import com.mak.feastit.remote.FeastAPIService
import com.mak.feastit.remote.dto.RecipeDTO

internal class RecipeNetworkSource(
    private val apiService: FeastAPIService
) : IRecipeNetworkSource {

    override suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDTO> {
        val response = apiService.searchRecipes(queryParams)
        val apiRecipes = response.results
        var recipeDetails = emptyList<RecipeDTO>()
        if (!apiRecipes.isNullOrEmpty()) {
            recipeDetails = apiRecipes
        }
        return recipeDetails
    }

    override suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDTO> {
        val response = apiService.searchRecipes(queryParams)
        val apiRecipes = response.results
        var recipeDetails = emptyList<RecipeDTO>()
        if (!apiRecipes.isNullOrEmpty()) {
            recipeDetails = apiRecipes
        }
        return recipeDetails
    }

}