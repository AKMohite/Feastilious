package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class FavRecipeUseCase constructor(
    private val recipeRepository: IRecipeRepository
) : UseCaseWithParams<String, Flow<RecipeResult<List<Recipe>>>>() {

    override fun buildUseCase(params: String): Flow<RecipeResult<List<Recipe>>> {
        return recipeRepository.getFavRecipes(params)
    }

}