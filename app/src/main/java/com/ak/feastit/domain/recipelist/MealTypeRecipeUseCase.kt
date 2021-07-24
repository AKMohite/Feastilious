package com.ak.feastit.domain.recipelist

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.repository.RecipeRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class MealTypeRecipeUseCase (
    private val repository: RecipeRepository
): UseCaseWithParams<HashMap<String, String>, Flow<RecipeResult<List<Recipe>>>>() {

    override fun buildUseCase(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.getRecipesByCategory(params)
    }
}