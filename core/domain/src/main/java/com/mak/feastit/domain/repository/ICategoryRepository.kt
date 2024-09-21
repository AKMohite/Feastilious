package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.MealType
import com.mak.feastit.domain.model.RecipeResult
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getFoodCategories(): Flow<RecipeResult<List<MealType>>>
}