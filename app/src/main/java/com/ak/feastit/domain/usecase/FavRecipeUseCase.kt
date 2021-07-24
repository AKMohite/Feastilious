package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class FavRecipeUseCase constructor(
    private val recipeRepository: IRecipeRepository
) {

    operator fun invoke(params: String): Flow<RecipeResult<List<Recipe>>> {
        return recipeRepository.getFavRecipes(params)
    }

}