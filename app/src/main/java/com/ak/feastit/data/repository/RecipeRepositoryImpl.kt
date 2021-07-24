package com.ak.feastit.data.repository

import android.util.Log
import com.ak.feastit.data.local.datasource.RecipeLocalDataSource
import com.ak.feastit.data.network.datasource.RecipeNetworkSource
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.repository.RecipeRepository
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import com.ak.feastit.utils.QUERY_SEARCH
import com.ak.feastit.utils.QUERY_TYPE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepositoryImpl constructor(
    private val networkSource: RecipeNetworkSource,
    private val localDataSource: RecipeLocalDataSource
) : RecipeRepository {

    override fun searchRecipes(
        params: HashMap<String, String>
    ): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val searchQuery = params[QUERY_SEARCH]?.toLowerCase() ?: ""
            val apiRecipes = networkSource.searchRecipes(params)
            localDataSource.saveNetworkRecipes(apiRecipes)
            val recipes: List<Recipe> = localDataSource.searchLocalRecipes(searchQuery)
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }

    override fun getRecipesByCategory(
        params: HashMap<String, String>
    ): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val mealType = params[QUERY_TYPE]?.toLowerCase() ?: ""
            val apiRecipes = networkSource.searchRecipes(params)
            localDataSource.saveNetworkRecipes(apiRecipes)
            val recipes: List<Recipe> = localDataSource.searchLocalRecipesByMealType(mealType)
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }
}