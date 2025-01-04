package com.mak.feastit.domain.legacyusecase

import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyRecipeDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LegacyToggleFavUseCase @Inject constructor(
    private val repository: ILegacyRecipeDetailRepository
) {
    operator fun invoke(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return repository.toggleRecipeFav(id, isAdded)
    }
}