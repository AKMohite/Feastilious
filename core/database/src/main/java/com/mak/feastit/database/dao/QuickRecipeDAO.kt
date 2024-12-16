package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.QuickRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickRecipeDAO: SectionRecipeDAO<QuickRecipeEntity> {

    @Query("SELECT r.* FROM quick_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
    override fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    @Query("DELETE FROM quick_recipes WHERE page = :page")
    override suspend fun deletePage(page: Int)

    @Query("DELETE FROM quick_recipes")
    override suspend fun deleteRecipes()
}