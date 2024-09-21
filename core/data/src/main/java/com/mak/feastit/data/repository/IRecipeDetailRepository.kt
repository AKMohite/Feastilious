package com.mak.feastit.data.repository

import com.mak.feastit.data.model.RecipeDetail
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface IRecipeDetailRepository {
    fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>>
    fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>>
}