package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDomainMapper
import com.mak.feastit.data.mapper.RecipeEntityMapper
import com.mak.feastit.data.model.Recipe
import com.mak.feastit.data.utils.QUERY_SEARCH
import com.mak.feastit.data.utils.QUERY_TYPE
import com.mak.feastit.data.utils.RecipeResult
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import com.mak.feastit.remote.FeastAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class RecipeRepository @Inject constructor(
    private val api: FeastAPIService,
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
            val apiRecipes = api.searchRecipes(params).results ?: emptyList()
            saveToLocal(entityMapper.toEntityList(apiRecipes))
            val recipes: List<Recipe> = domainMapper.toRecipesDomain(db.recipeDAO().searchRecipes(searchQuery))
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
        }
    }

    override fun getRecipesByCategory(
        params: HashMap<String, String>
    ): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val mealType = params[QUERY_TYPE]?.toLowerCase() ?: ""
            val apiRecipes = api.searchRecipes(params).results ?: emptyList()
            saveToLocal(entityMapper.toEntityList(apiRecipes))
            val recipes: List<Recipe> = domainMapper.toRecipesDomain(db.recipeDAO().getRecipesByMealType(mealType))
            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
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
        }
    }
}