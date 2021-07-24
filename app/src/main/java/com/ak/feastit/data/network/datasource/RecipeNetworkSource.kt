package com.ak.feastit.data.network.datasource

import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.domain.mapper.RecipeEntityMapper
import com.ak.feastit.domain.datasource.IRecipeNetworkSource

class RecipeNetworkSource(
    private val apiService: FeastAPIService,
    private val recipeEntityMapper: RecipeEntityMapper
) : IRecipeNetworkSource {

    override suspend fun searchRecipes(queryParams: HashMap<String, String>): List<RecipeDetailEntity> {
        val response = apiService.searchRecipes(queryParams)
        val apiRecipes = response.results
        var recipeDetails = emptyList<RecipeDetailEntity>()
        if (!apiRecipes.isNullOrEmpty()) {
            recipeDetails = recipeEntityMapper.toEntityList(apiRecipes)
        }
        return recipeDetails
    }

    override suspend fun getRecipesByMealType(queryParams: HashMap<String, String>): List<RecipeDetailEntity> {
        val response = apiService.searchRecipes(queryParams)
        val apiRecipes = response.results
        var recipeDetails = emptyList<RecipeDetailEntity>()
        if (!apiRecipes.isNullOrEmpty()) {
            recipeDetails = recipeEntityMapper.toEntityList(apiRecipes)
        }
        return recipeDetails
    }

}