package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyRecipeDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LegacyRecipeDetailUseCase @Inject constructor(
        private val repository: ILegacyRecipeDetailRepository
) {

    operator fun invoke(params: Long): Flow<RecipeResult<RecipeDetail>> {
        return repository.getRecipeDetail(params)
    }
}