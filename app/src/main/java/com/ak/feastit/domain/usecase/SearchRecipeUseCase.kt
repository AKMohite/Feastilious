package com.ak.feastit.domain.usecase

import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class SearchRecipeUseCase (
    private val repository: IRecipeRepository
) {
    operator fun invoke(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.searchRecipes(params)
    }
}