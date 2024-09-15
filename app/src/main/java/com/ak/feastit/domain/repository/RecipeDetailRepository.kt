package com.ak.feastit.domain.repository

import android.util.Log
import com.ak.feastit.domain.mapper.RecipeDomainMapper
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import com.mak.feastit.database.FeastDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeDetailRepository @Inject constructor(
    private val db: FeastDB
) : IRecipeDetailRepository {

    private val domainMapper = RecipeDomainMapper()

    override fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>> = flow {
        try {
            emit(RecipeResult.loading())
            val recipe = domainMapper.toRecipeDetailDomain(db.recipeDAO().getRecipeDetail(recipeId = id))
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
                val isUpdated = (db.recipeDAO().toggleFav(id, isAdded) > 0)
                emit(RecipeResult.success(isUpdated))
            } catch (error: Exception) {
                emit(RecipeResult.error(error.message ?: "An error occurred"))
                Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
            }
        }
    }
}