package com.ak.feastit.domain.repository

import android.util.Log
import com.ak.feastit.data.network.datasource.IRecipeNetworkSource
import com.ak.feastit.domain.mapper.RecipeDomainMapper
import com.ak.feastit.domain.mapper.RecipeEntityMapper
import com.ak.feastit.domain.model.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import com.ak.feastit.utils.QUERY_SEARCH
import com.ak.feastit.utils.QUERY_TYPE
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(
        private val networkSource: IRecipeNetworkSource,
    private val db: FeastDB
) : IRecipeRepository {

    private val domainMapper = RecipeDomainMapper()
    private val entityMapper = RecipeEntityMapper()

    override fun searchRecipes(
        params: HashMap<String, String>
    ): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val searchQuery = params[QUERY_SEARCH]?.toLowerCase() ?: ""
            val apiRecipes = networkSource.searchRecipes(params)
            saveToLocal(entityMapper.toEntityList(apiRecipes))
            val recipes: List<Recipe> = domainMapper.toRecipesDomain(db.recipeDAO().searchRecipes(searchQuery))
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
            saveToLocal(entityMapper.toEntityList(apiRecipes))
            val recipes: List<Recipe> = domainMapper.toRecipesDomain(db.recipeDAO().getRecipesByMealType(mealType))
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }

    private suspend fun saveToLocal(recipeDetails: List<RecipeDetailEntity>) {
        if (recipeDetails.isNotEmpty()) {
            val dtoRecipes: MutableList<RecipeEntity> = mutableListOf()
            val ingredients: MutableList<IngredientEntity> = mutableListOf()
            val instructions: MutableList<InstructionEntity> = mutableListOf()

//            TODO handle favorite recipes while deleting from DB and null assertion
            val favRecipes = db.recipeDAO().getFavRecipeIds()
            recipeDetails.forEach { recipe ->
                if (favRecipes.isNotEmpty() && recipe.recipe.id in favRecipes)
                    dtoRecipes.add(recipe.recipe.copy(isAdded = true))
                else
                    dtoRecipes.add(recipe.recipe)
                ingredients.addAll(recipe.ingredients)
                instructions.addAll(recipe.instructions)
            }
            db.blockTransaction {
                with(db.recipeDAO()) {
                    insertRecipes(dtoRecipes)
                    insertIngredients(ingredients)
                    insertInstructions(instructions)
                }
            }
        }
    }

    override fun getFavRecipes(
        params: String
    ): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val recipes = domainMapper.toRecipesDomain(db.recipeDAO().getFavRecipes(params))
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getFavRecipes: ${e.message}")
        }
    }
}