package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.LegacyRecipeDomainMapper
import com.mak.feastit.data.mapper.LegacyRecipeEntityMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import com.mak.feastit.domain.model.QUERY_SEARCH
import com.mak.feastit.domain.model.QUERY_TYPE
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyRecipeRepository
import com.mak.feastit.remote.FeastAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LegacyRecipeRepository @Inject constructor(
    private val api: FeastAPIService,
    private val db: FeastDB
) : ILegacyRecipeRepository {

    private val domainMapper = LegacyRecipeDomainMapper()
    private val entityMapper = LegacyRecipeEntityMapper()

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
            val instructions: MutableList<RecipeStepEntity> = mutableListOf()

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