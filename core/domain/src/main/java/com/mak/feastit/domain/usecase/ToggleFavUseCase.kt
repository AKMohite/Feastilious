package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.IRecipeDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ToggleFavUseCase @Inject constructor(
    private val repository: IRecipeDetailRepository
) {
    operator fun invoke(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return repository.toggleRecipeFav(id, isAdded)
    }
}