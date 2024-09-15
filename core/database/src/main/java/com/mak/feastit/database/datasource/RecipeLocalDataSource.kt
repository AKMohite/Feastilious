package com.mak.feastit.database.datasource

import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity

internal class RecipeLocalDataSource(
    private val feastDatabase: FeastDB
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
            feastDatabase.blockTransaction {
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