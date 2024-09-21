package com.mak.feastit.data.repository

import com.mak.feastit.data.model.MealType
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getFoodCategories(): Flow<RecipeResult<List<MealType>>>
}