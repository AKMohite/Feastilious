package com.ak.feastit.domain.recipedetails

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.repository.RecipeDetailRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class RecipeDetailUseCase constructor(
        private val repository: RecipeDetailRepository
): UseCaseWithParams<Long, Flow<RecipeResult<RecipeDetail>>>() {

    override fun buildUseCase(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}