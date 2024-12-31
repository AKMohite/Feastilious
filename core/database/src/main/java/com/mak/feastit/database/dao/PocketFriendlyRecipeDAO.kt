package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.PocketFriendlyRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketFriendlyRecipeDAO: SectionRecipeDAO<PocketFriendlyRecipeEntity> {

    @Query("SELECT r.* FROM pocket_friendly_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
    override fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    @Query("DELETE FROM pocket_friendly_recipes WHERE page = :page")
    override suspend fun deletePage(page: Int)

    @Query("DELETE FROM pocket_friendly_recipes")
    override suspend fun deleteRecipes()

    @Query("SELECT recipe_id FROM pocket_friendly_recipes")
    override suspend fun getAllIds(): List<Long>

    @Query("SELECT COUNT(*) FROM pocket_friendly_recipes WHERE page = :page LIMIT 1")
    suspend fun getCount(page: Int): Int
}