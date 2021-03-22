package com.ak.feastit.domain.recipedetails

import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface RecipeDetailRepository {

    fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>>
    fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>>
}