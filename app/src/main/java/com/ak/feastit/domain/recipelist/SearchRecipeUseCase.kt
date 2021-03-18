package com.ak.feastit.domain.recipelist

import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class SearchRecipeUseCase (
    private val repository: RecipeRepository
): UseCaseWithParams<HashMap<String, String>, Flow<RecipeResult<List<Recipe>>>>() {

    override fun buildUseCase(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> {
        return repository.searchRecipes(params)
    }
}