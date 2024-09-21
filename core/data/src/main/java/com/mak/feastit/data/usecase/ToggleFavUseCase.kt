package com.mak.feastit.data.usecase

import com.mak.feastit.data.repository.IRecipeDetailRepository
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class ToggleFavUseCase constructor(
    private val repository: IRecipeDetailRepository
) {
    operator fun invoke(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return repository.toggleRecipeFav(id, isAdded)
    }
}