// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SimilarRecipeDAO : BaseDAO<SimilarRecipeEntity> {
  @Query("DELETE FROM similar_recipes WHERE parent_id = :recipeId")
  suspend fun deleteRecipe(recipeId: Long)

  @Query("SELECT COUNT(*) FROM similar_recipes WHERE parent_id = :id")
  suspend fun getCountForRecipe(id: Long): Int

  @Query("SELECT r.* FROM similar_recipes s INNER JOIN recipes r ON s.recipe_id = r.id WHERE s.parent_id = :id")
  fun getRecipes(id: Long): Flow<List<RecipeEntity>>

  @Query("SELECT recipe_id FROM similar_recipes")
  suspend fun getAllIds(): List<Long>
}
