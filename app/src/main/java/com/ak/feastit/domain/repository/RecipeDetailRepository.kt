package com.ak.feastit.domain.repository

import android.util.Log
import com.mak.feastit.database.datasource.IRecipeLocalDataSource
import com.ak.feastit.domain.mapper.RecipeDomainMapper
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeDetailRepository constructor(
    private val localDataSource: com.mak.feastit.database.datasource.IRecipeLocalDataSource
) : IRecipeDetailRepository {

    private val domainMapper = RecipeDomainMapper()

    override fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>> = flow {
        try {
            emit(RecipeResult.loading())
            val recipe = domainMapper.toRecipeDetailDomain(localDataSource.getRecipeDetail(recipeId = id))
            emit(RecipeResult.success(recipe))
        } catch (error: Exception) {
            emit(RecipeResult.error(error.message ?: "An error occurred"))
            Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
        }
    }

    override fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return flow {
            try {
                emit(RecipeResult.loading())
                val isUpdated = localDataSource.toggleFav(recipeId = id, isFav = isAdded)
                emit(RecipeResult.success(isUpdated))
            } catch (error: Exception) {
                emit(RecipeResult.error(error.message ?: "An error occurred"))
                Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
            }
        }
    }
}