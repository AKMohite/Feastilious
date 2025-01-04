package com.mak.feastit.domain.legacyusecase

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LegacyFavRecipeUseCase @Inject constructor(
    private val recipeRepository: ILegacyRecipeRepository
) {

    operator fun invoke(params: String): Flow<RecipeResult<List<Recipe>>> {
        return recipeRepository.getFavRecipes(params)
    }

}