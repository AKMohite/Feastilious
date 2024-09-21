package com.mak.feastit.data.usecase

import com.mak.feastit.data.model.Recipe
import com.mak.feastit.data.repository.IRecipeRepository
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class FavRecipeUseCase constructor(
    private val recipeRepository: IRecipeRepository
) {

    operator fun invoke(params: String): Flow<RecipeResult<List<Recipe>>> {
        return recipeRepository.getFavRecipes(params)
    }

}