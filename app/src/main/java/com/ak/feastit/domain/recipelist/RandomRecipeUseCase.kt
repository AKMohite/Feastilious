package com.ak.feastit.domain.recipelist

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class RandomRecipeUseCase (
    private val repository: RecipeRepository
): UseCaseWithParams<Unit, Flow<RecipeResult<List<Recipe>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<Recipe>>> {
        return repository.getRandomRecipes()
    }
}