package com.mak.feastit.data.repository

import com.mak.feastit.data.model.Recipe
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getRecipesByCategory(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getFavRecipes(params: String): Flow<RecipeResult<List<Recipe>>>
}
