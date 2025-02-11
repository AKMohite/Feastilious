package com.mak.feastit.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularRecipeDAO: SectionRecipeDAO<PopularRecipeEntity> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override suspend fun insert(entities: List<PopularRecipeEntity>)

    @Query("SELECT r.* FROM popular_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
    override fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    @Query("DELETE FROM popular_recipes WHERE page = :page")
    override suspend fun deletePage(page: Int)

    @Query("DELETE FROM popular_recipes")
    override suspend fun deleteRecipes()

    @Query("SELECT recipe_id FROM popular_recipes")
    override suspend fun getAllIds(): List<Long>

    @Query("SELECT COUNT(*) FROM popular_recipes WHERE page = :page LIMIT 1")
    suspend fun getCount(page: Int): Int

    @Query("SELECT r.* FROM popular_recipes p INNER JOIN recipes r ON p.recipe_id = r.id")
    fun pagedRecipes(): PagingSource<Int, RecipeEntity>
}

