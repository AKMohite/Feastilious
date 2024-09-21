package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.IRecipeDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecipeDetailUseCase @Inject constructor(
        private val repository: IRecipeDetailRepository
) {

    operator fun invoke(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}