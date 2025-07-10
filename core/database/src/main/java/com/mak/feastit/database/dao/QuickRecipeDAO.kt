package com.mak.feastit.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.QuickRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickRecipeDAO: SectionRecipeDAO<QuickRecipeEntity> {

    @Query("SELECT r.* FROM quick_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
    override fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    @Query("DELETE FROM quick_recipes WHERE page = :page")
    override suspend fun deletePage(page: Int)

    @Query("DELETE FROM quick_recipes")
    override suspend fun deleteRecipes()

    @Query("SELECT recipe_id FROM quick_recipes")
    override suspend fun getAllIds(): List<Long>

    @Query("SELECT COUNT(*) FROM quick_recipes WHERE page = :page LIMIT 1")
    suspend fun getCount(page: Int): Int

    @Query("SELECT r.id, r.name, r.summary, r.img, p.page FROM quick_recipes p INNER JOIN recipes r ON p.recipe_id = r.id  ORDER BY p.page ASC")
    fun pagedRecipes(): PagingSource<Int, PaginatedRecipeEntity>
}