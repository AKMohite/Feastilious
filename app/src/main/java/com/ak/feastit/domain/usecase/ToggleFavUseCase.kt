package com.ak.feastit.domain.recipedetails

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.repository.RecipeDetailRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow


class ToggleFavUseCase constructor(
    private val repository: RecipeDetailRepository
): UseCaseWithParams<FavParams, Flow<RecipeResult<Boolean>>>() {

    override fun buildUseCase(params: FavParams): Flow<RecipeResult<Boolean>> {
        return repository.toggleRecipeFav(params.id, params.isAdded)
    }
}

data class FavParams(
        val id: Long,
        val isAdded: Boolean
)