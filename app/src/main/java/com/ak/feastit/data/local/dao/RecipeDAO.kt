package com.ak.feastit.data.local.dao

import androidx.room.*
import com.ak.feastit.data.local.entity.IngredientEntity
import com.ak.feastit.data.local.entity.InstructionEntity
import com.ak.feastit.data.local.entity.RecipeEntity
import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.utils.*

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

    @Query("SELECT * FROM $DB_RECIPE_TABLE")
    suspend fun getRecipes(): List<RecipeEntity>

    @Query("SELECT $DB_TABLE_ID FROM $DB_RECIPE_TABLE WHERE $DB_MY_RECIPE_BOOK = 1")
    suspend fun getFavRecipeIds(): List<Long>

    @Transaction
    @Query("SELECT * FROM $DB_RECIPE_TABLE WHERE $DB_TABLE_ID= :recipeId")
    suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity

    @Query("UPDATE $DB_RECIPE_TABLE SET $DB_MY_RECIPE_BOOK = :isFav WHERE $DB_TABLE_ID = :id")
    suspend fun toggleFav(id: Long, isFav: Boolean): Int

    @Query("""SELECT * FROM $DB_RECIPE_TABLE WHERE 
                    LOWER($DB_TABLE_COL_NAME) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_TABLE_COL_SUMMARY) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_CUISINES) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_DISH_TYPES) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_DIETS) LIKE '%' || :searchQuery || '%'""")
    suspend fun searchRecipes(searchQuery: String): List<RecipeEntity>

    @Query("""SELECT * FROM $DB_RECIPE_TABLE WHERE LOWER($DB_RECIPE_DISH_TYPES) LIKE '%'|| :mealType ||'%'""")
    suspend fun getRecipesByMealType(mealType: String): List<RecipeEntity>

    @Query("""SELECT * FROM $DB_RECIPE_TABLE WHERE $DB_MY_RECIPE_BOOK = 1 AND
                    (LOWER($DB_TABLE_COL_NAME) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_TABLE_COL_SUMMARY) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_CUISINES) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_DISH_TYPES) LIKE '%' || :searchQuery || '%' OR 
                    LOWER($DB_RECIPE_DIETS) LIKE '%' || :searchQuery || '%')""")
    suspend fun getFavRecipes(searchQuery: String): List<RecipeEntity>

//    Delete only if recipe is not added in book and delete all ingredients and instructions
}