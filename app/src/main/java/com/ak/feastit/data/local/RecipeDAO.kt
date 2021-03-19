package com.ak.feastit.data.local

import androidx.room.*
import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.utils.DB_RECIPE_ID

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredient: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstruction(instruction: InstructionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructions(instructions: List<InstructionEntity>)

    @Transaction
    @Query("SELECT * FROM recipes WHERE $DB_RECIPE_ID= :recipeId")
    suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity

//    Delete only if recipe is not added in book and delete all ingredients and instructions
}