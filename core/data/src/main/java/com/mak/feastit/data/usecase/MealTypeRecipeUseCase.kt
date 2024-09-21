package com.mak.feastit.data.usecase

import com.mak.feastit.data.model.Recipe
import com.mak.feastit.data.repository.IRecipeRepository
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class MealTypeRecipeUseCase (
    private val repository: IRecipeRepository
) {
    operator fun invoke(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.getRecipesByCategory(params)
    }
}