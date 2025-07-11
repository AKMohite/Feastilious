// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.HealthyRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthyRecipeDAO : SectionRecipeDAO<HealthyRecipeEntity> {
  //    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override suspend fun insert(entities: List<PopularRecipeEntity>)

  @Query("SELECT r.* FROM healthy_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
  override fun getRecipes(page: Int): Flow<List<RecipeEntity>>

  @Query("DELETE FROM healthy_recipes WHERE page = :page")
  override suspend fun deletePage(page: Int)

  @Query("DELETE FROM healthy_recipes")
  override suspend fun deleteRecipes()

  @Query("SELECT recipe_id FROM healthy_recipes")
  override suspend fun getAllIds(): List<Long>

  @Query("SELECT COUNT(*) FROM healthy_recipes WHERE page = :page LIMIT 1")
  suspend fun getCount(page: Int): Int

  @Query(
    "SELECT r.id, r.name, r.summary, r.img, p.page FROM healthy_recipes p INNER JOIN recipes r ON p.recipe_id = r.id  ORDER BY p.page ASC",
  )
  fun pagedRecipes(): PagingSource<Int, PaginatedRecipeEntity>
}
