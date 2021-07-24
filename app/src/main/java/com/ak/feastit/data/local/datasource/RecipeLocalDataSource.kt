package com.ak.feastit.data.local.datasource

import androidx.room.withTransaction
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.IngredientEntity
import com.ak.feastit.data.local.InstructionEntity
import com.ak.feastit.data.local.RecipeEntity
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.domain.datasource.IRecipeLocalDataSource
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.model.Recipe

class RecipeLocalDataSource(
    private val feastDatabase: FeastDatabase,
    private val recipeDomainMapper: RecipeDomainMapper
) : IRecipeLocalDataSource {

    private val recipeDAO = feastDatabase.recipeDAO()

    override suspend fun saveNetworkRecipes(recipeDetails: List<RecipeDetailEntity>): Boolean {
        if (!recipeDetails.isNullOrEmpty()) {
            val dtoRecipes: MutableList<RecipeEntity> = mutableListOf()
            val ingredients: MutableList<IngredientEntity> = mutableListOf()
            val instructions: MutableList<InstructionEntity> = mutableListOf()

//            TODO handle favorite recipes while deleting from DB and null assertion
            val favRecipes = recipeDAO.getFavRecipeIds()
            recipeDetails.forEach { recipe ->
                if (!favRecipes.isNullOrEmpty() && recipe.recipe.id in favRecipes)
                    dtoRecipes.add(recipe.recipe.copy(isAdded = true))
                else
                    dtoRecipes.add(recipe.recipe)
                ingredients.addAll(recipe.ingredients)
                instructions.addAll(recipe.instructions)
            }
            feastDatabase.withTransaction {
                recipeDAO.insertRecipes(dtoRecipes)
                recipeDAO.insertIngredients(ingredients)
                recipeDAO.insertInstructions(instructions)
            }
        }

        return true
    }

    override suspend fun searchLocalRecipes(searchQuery: String): List<Recipe> {
        val localRecipes = recipeDAO.searchRecipes(searchQuery)
        return recipeDomainMapper.toRecipesDomain(localRecipes)
    }

    override suspend fun searchLocalRecipesByMealType(mealType: String): List<Recipe> {
        val localRecipes = recipeDAO.getRecipesByMealType(mealType)
        return recipeDomainMapper.toRecipesDomain(localRecipes)
    }

    override suspend fun getRecipeDetail(recipeId: Long): RecipeDetail {
        val recipeDetail = recipeDAO.getRecipeDetail(recipeId)
        return recipeDomainMapper.toRecipeDetailDomain(recipeDetail)
    }

    override suspend fun toggleFav(recipeId: Long, isFav: Boolean): Boolean {
        val rowsUpdated = recipeDAO.toggleFav(recipeId, isFav)
        return rowsUpdated > 0
    }
}