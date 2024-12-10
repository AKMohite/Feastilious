
package com.ak.feastit.data.network.datasource

import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.data.network.dto.RecipeDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeNetworkSource(
    private val apiService: FeastAPIService
) : IRecipeNetworkSource {

    override suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDTO> = withContext(Dispatchers.IO) {
        val response = apiService.searchRecipes(queryParams)
        response.results ?: emptyList()
    }
    override suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDTO> = withContext(Dispatchers.IO) {
        val response = apiService.searchRecipes(queryParams)
        val apiRecipes = response.results
        var recipeDetails = emptyList<RecipeDTO>()
        if (!apiRecipes.isNullOrEmpty()) {
            recipeDetails = apiRecipes
        }
        recipeDetails
    }

}