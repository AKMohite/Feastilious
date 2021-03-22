package com.ak.feastit.domain.recipedetails

import android.util.Log
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeDetailRepositoryImpl constructor(
        private val feastDatabase: FeastDatabase,
        private val domainMapper: RecipeDomainMapper
) : RecipeDetailRepository {

    private val recipeDAO = feastDatabase.recipeDAO()

    override fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>> = flow {
        try {
            emit(RecipeResult.loading())
            val recipe = recipeDAO.getRecipeDetail(recipeId = id)

            emit(RecipeResult.success(domainMapper.toRecipeDetailDomain(recipe)))
        } catch (error: Exception) {
            emit(RecipeResult.error(error.message ?: "An error occurred"))
            Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
        }
    }

    override fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return flow {
            try {
                emit(RecipeResult.loading())
                val isUpdated = recipeDAO.toggleFav(id, isAdded)
                emit(RecipeResult.success(isUpdated > 0))
            } catch (error: Exception) {
                emit(RecipeResult.error(error.message ?: "An error occurred"))
                Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
            }
        }
    }
}