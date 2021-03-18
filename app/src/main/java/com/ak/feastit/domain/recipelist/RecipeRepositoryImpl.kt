package com.ak.feastit.domain.recipelist

import android.util.Log
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeRepositoryImpl constructor(
    private val apiService: FeastAPIService
) : RecipeRepository {

    override fun getRandomRecipes(): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val response = apiService.getRandomRecipes()
            val recipes = mutableListOf<Recipe>()
            response.recipes?.map { recipe ->
                recipes.add(
                    Recipe(
                        id = recipe.id,
                        recipeName = recipe.title,
                        recipeImgUrl = recipe.image
                    )
                )
            }

            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }

    override fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val response = apiService.searchRecipes(params)
            val recipes = mutableListOf<Recipe>()
            response.results?.map { recipe ->
                recipes.add(
                        Recipe(
                                id = recipe.id,
                                recipeName = recipe.title,
                                recipeImgUrl = recipe.image
                        )
                )
            }

            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }
}