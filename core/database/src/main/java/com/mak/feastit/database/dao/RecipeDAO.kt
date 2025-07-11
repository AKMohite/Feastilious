// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO : BaseDAO<RecipeEntity> {
  @Query("SELECT id FROM recipes WHERE is_fav = 1")
  suspend fun getFavRecipeIds(): List<Long>

  @Transaction
  @Query("SELECT * FROM recipes WHERE id= :recipeId")
  suspend fun getRecipeDetail(recipeId: Long): RecipeDetailEntity

  @Query("UPDATE recipes SET is_fav = :isFav WHERE id = :id")
  suspend fun toggleFav(
    id: Long,
    isFav: Boolean,
  ): Int

  @Query(
    """SELECT * FROM recipes WHERE 
                    LOWER(name) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(summary) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(cuisines) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(dish_types) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(diets) LIKE '%' || :searchQuery || '%'""",
  )
  suspend fun searchRecipes(searchQuery: String): List<RecipeEntity>

  @Query("""SELECT * FROM recipes WHERE LOWER(dish_types) LIKE '%'|| :mealType ||'%'""")
  suspend fun getRecipesByMealType(mealType: String): List<RecipeEntity>

  @Query(
    """SELECT * FROM recipes WHERE is_fav = 1 AND
                    (LOWER(name) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(summary) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(cuisines) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(dish_types) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(diets) LIKE '%' || :searchQuery || '%')""",
  )
  suspend fun getFavRecipes(searchQuery: String): List<RecipeEntity>

  @Query(
    """SELECT * FROM recipes WHERE is_fav = 1 AND
                    (LOWER(name) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(summary) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(cuisines) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(dish_types) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(diets) LIKE '%' || :searchQuery || '%') LIMIT 50""",
  )
  fun searchFavRecipes(searchQuery: String): Flow<List<RecipeEntity>>

  @Query("SELECT * FROM recipes WHERE id = :id")
  fun getRecipe(id: Long): Flow<RecipeEntity?>

  @Query("SELECT * FROM recipes WHERE id IN (:ids)")
  suspend fun getRecipes(ids: List<Long>): List<RecipeEntity>

  @Query("DELETE FROM recipes WHERE id NOT IN (:neededIds) AND is_fav = 0")
  suspend fun deleteRecipesNotIn(neededIds: Set<Long>)

  @Query("SELECT * FROM recipes WHERE is_fav = 1")
  fun observeFavoriteRecipes(): Flow<List<RecipeEntity>>

  @Query(
    """SELECT * FROM recipes WHERE is_fav = 1 AND
                    (LOWER(name) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(summary) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(cuisines) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(dish_types) LIKE '%' || :searchQuery || '%' OR 
                    LOWER(diets) LIKE '%' || :searchQuery || '%')""",
  )
  fun observeQueryRecipes(searchQuery: String): PagingSource<Int, RecipeEntity>

//    Delete only if recipe is not added in book and delete all ingredients and instructions
}
