package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class ToggleFavUseCase constructor(
    private val repository: IRecipeDetailRepository
) {
    operator fun invoke(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return repository.toggleRecipeFav(id, isAdded)
    }
}