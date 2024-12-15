package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularRecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<PopularRecipeEntity>)

    @Query("SELECT r.* FROM popular_recipes p INNER JOIN recipes r ON p.recipe_id = r.id WHERE p.page = :page")
    fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    @Query("DELETE FROM popular_recipes WHERE page = :page")
    fun deletePage(page: Int)

    @Query("DELETE FROM popular_recipes")
    fun deletePopularRecipes()
}