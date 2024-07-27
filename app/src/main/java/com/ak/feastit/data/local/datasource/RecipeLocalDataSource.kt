package com.ak.feastit.data.local.datasource

import androidx.room.withTransaction
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.entity.IngredientEntity
import com.ak.feastit.data.local.entity.InstructionEntity
import com.ak.feastit.data.local.entity.RecipeEntity
import com.ak.feastit.data.local.relations.RecipeDetailEntity

class RecipeLocalDataSource(
    private val feastDatabase: FeastDatabase
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

    override suspend fun searchLocalRecipes(searchQuery: String): List<RecipeEntity> {
        return recipeDAO.searchRecipes(searchQuery)
    }

    override suspend fun getFavRecipes(searchQuery: String): List<RecipeEntity> {
        return recipeDAO.getFavRecipes(searchQuery)
    }

    override suspend fun searchLocalRecipesByMealType(mealType: String): List<RecipeEntity> {
        return recipeDAO.getRecipesByMealType(mealType)
    }

    override suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity {
        return recipeDAO.getRecipeDetail(recipeId)
    }

    override suspend fun toggleFav(recipeId: Long, isFav: Boolean): Boolean {
        val rowsUpdated = recipeDAO.toggleFav(recipeId, isFav)
        return rowsUpdated > 0
    }
}