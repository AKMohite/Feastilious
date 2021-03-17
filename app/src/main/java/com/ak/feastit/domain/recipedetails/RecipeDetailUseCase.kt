package com.ak.feastit.domain.recipedetails

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class RecipeDetailUseCase constructor(
        private val repository: RecipeDetailRepository
): UseCaseWithParams<Recipe, Flow<RecipeResult<RecipeDetail>>>() {

    override fun buildUseCase(params: Recipe): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}