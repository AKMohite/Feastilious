package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getRecipesByCategory(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
    fun getFavRecipes(params: String): Flow<RecipeResult<List<Recipe>>>
}
