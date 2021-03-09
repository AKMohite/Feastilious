package com.ak.feastit.domain.category

import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getFoodCategories(): Flow<RecipeResult<List<RecipeCategory>>>

}