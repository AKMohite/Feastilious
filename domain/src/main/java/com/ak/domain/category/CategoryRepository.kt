package com.ak.domain.category

import com.ak.domain.utils.RecipeResult

interface CategoryRepository {

    fun getFoodCategories(): RecipeResult<List<RecipeCategory>>

}