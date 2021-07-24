package com.ak.feastit.domain.repository

import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getRecipesByCategory(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getFavRecipes(params: String): Flow<RecipeResult<List<Recipe>>>
}
