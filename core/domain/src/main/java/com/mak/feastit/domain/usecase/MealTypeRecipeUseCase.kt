package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.IRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MealTypeRecipeUseCase @Inject constructor(
    private val repository: IRecipeRepository
) {
    operator fun invoke(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.getRecipesByCategory(params)
    }
}