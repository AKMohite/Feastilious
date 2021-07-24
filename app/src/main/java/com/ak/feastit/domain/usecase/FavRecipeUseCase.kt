package com.ak.feastit.domain.usecase

import android.util.Log
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.domain.mapper.RecipeDomainMapper
import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavRecipeUseCase constructor(
        private val feastDatabase: FeastDatabase,
        private val recipeDomainMapper: RecipeDomainMapper
) : UseCaseWithParams<String, Flow<RecipeResult<List<Recipe>>>>() {

    private val recipeDAO = feastDatabase.recipeDAO()

    override fun buildUseCase(params: String): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            var recipes = listOf<Recipe>()
            val localRecipes = recipeDAO.getFavRecipes(params.toLowerCase())
            recipes = recipeDomainMapper.toRecipesDomain(localRecipes)
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getFavRecipes: ${e.message}")
        }
    }

}