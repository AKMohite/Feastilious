package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import com.mak.feastit.database.util.Constants.DB_MY_RECIPE_BOOK
import com.mak.feastit.database.util.Constants.DB_RECIPE_CUISINES
import com.mak.feastit.database.util.Constants.DB_RECIPE_DIETS
import com.mak.feastit.database.util.Constants.DB_RECIPE_DISH_TYPES
import com.mak.feastit.database.util.Constants.DB_RECIPE_TABLE
import com.mak.feastit.database.util.Constants.DB_TABLE_COL_NAME
import com.mak.feastit.database.util.Constants.DB_TABLE_COL_SUMMARY
import com.mak.feastit.database.util.Constants.DB_TABLE_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO: BaseDAO<RecipeEntity> {

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

    @Query("SELECT * FROM $DB_RECIPE_TABLE WHERE $DB_TABLE_ID = :id")
    fun getRecipe(id: Long): Flow<RecipeEntity?>

    @Query("SELECT * FROM $DB_RECIPE_TABLE WHERE $DB_TABLE_ID IN (:ids)")
    suspend fun getRecipes(ids: List<Long>): List<RecipeEntity>

//    Delete only if recipe is not added in book and delete all ingredients and instructions
}