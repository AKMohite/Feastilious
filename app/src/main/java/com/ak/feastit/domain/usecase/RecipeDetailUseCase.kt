package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class RecipeDetailUseCase constructor(
        private val repository: IRecipeDetailRepository
): UseCaseWithParams<Long, Flow<RecipeResult<RecipeDetail>>>() {

    override fun buildUseCase(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}