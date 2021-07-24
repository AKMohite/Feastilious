package com.ak.feastit.domain.repository

import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface IRecipeDetailRepository {
    fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>>
    fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>>
}