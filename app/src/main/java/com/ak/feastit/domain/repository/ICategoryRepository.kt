package com.ak.feastit.domain.repository

import com.ak.feastit.domain.model.MealType
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getFoodCategories(): Flow<RecipeResult<List<MealType>>>
}