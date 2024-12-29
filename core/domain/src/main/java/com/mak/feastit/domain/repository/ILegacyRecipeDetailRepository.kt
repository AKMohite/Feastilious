package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.RecipeResult
import kotlinx.coroutines.flow.Flow

interface ILegacyRecipeDetailRepository {
    fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>>
    fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>>
}