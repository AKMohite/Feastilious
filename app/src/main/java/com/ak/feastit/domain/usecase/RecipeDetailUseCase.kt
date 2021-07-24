package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class RecipeDetailUseCase constructor(
        private val repository: IRecipeDetailRepository
) {

    operator fun invoke(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}