package com.ak.feastit.domain.recipelist

import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getRandomRecipes(): Flow<RecipeResult<List<Recipe>>>

    fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>>
}
