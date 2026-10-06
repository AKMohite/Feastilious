// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDAO : BaseDAO<IngredientEntity> {
  @Query("DELETE FROM recipe_ingredients WHERE recipe_id = :recipeId")
  suspend fun deleteRecipe(recipeId: Long)

  @Query("SELECT * FROM recipe_ingredients WHERE recipe_id = :id")
  fun getIngredientsFor(id: Long): Flow<List<IngredientEntity>>

  @Query("SELECT * FROM recipe_ingredients WHERE id = :id")
  suspend fun getIngredient(id: String): IngredientEntity?
}
