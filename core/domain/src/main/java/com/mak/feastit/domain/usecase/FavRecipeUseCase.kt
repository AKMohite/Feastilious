package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.IRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavRecipeUseCase @Inject constructor(
    private val recipeRepository: IRecipeRepository
) {

    operator fun invoke(params: String): Flow<RecipeResult<List<Recipe>>> {
        return recipeRepository.getFavRecipes(params)
    }

}