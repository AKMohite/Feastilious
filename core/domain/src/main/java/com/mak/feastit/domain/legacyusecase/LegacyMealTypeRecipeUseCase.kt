package com.mak.feastit.domain.legacyusecase

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LegacyMealTypeRecipeUseCase @Inject constructor(
    private val repository: ILegacyRecipeRepository
) {
    operator fun invoke(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.getRecipesByCategory(params)
    }
}