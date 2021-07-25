package com.ak.feastit.data.network.datasource

import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.data.network.dto.RecipeDTO

class RecipeNetworkSource(
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