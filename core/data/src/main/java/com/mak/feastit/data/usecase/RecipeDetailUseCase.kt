package com.mak.feastit.data.usecase

import com.mak.feastit.data.model.RecipeDetail
import com.mak.feastit.data.repository.IRecipeDetailRepository
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class RecipeDetailUseCase constructor(
        private val repository: IRecipeDetailRepository
) {

    operator fun invoke(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}