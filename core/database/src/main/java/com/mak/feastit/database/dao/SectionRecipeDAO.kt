// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.room.Dao
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionRecipeDAO<Entity> : BaseDAO<Entity> {
  fun getRecipes(page: Int): Flow<List<RecipeEntity>>

  suspend fun deletePage(page: Int)

  suspend fun deleteRecipes()

  suspend fun getAllIds(): List<Long>
}
