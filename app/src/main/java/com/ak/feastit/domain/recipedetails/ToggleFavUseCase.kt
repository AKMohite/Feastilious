package com.ak.feastit.domain.recipedetails

import android.util.Log
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ToggleFavUseCase constructor(
        feastDatabase: FeastDatabase
): UseCaseWithParams<FavParams, Flow<RecipeResult<Boolean>>>() {

    private val recipeDAO = feastDatabase.recipeDAO()

    override fun buildUseCase(params: FavParams): Flow<RecipeResult<Boolean>> = flow {
        try {
            emit(RecipeResult.loading())
            val isUpdated = recipeDAO.toggleFav(params.id, params.isAdded)
            emit(RecipeResult.success(isUpdated > 0))
        } catch (error: Exception) {
            emit(RecipeResult.error(error.message ?: "An error occurred"))
            Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
        }
    }
}

data class FavParams(
        val id: Long,
        val isAdded: Boolean
)