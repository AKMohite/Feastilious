package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mak.feastit.database.entity.PopularRecipeEntity

@Dao
interface PopularRecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<PopularRecipeEntity>)

    @Query("DELETE FROM popular_recipes WHERE page = :page")
    fun deletePage(page: Int)

    @Query("DELETE FROM popular_recipes")
    fun deletePopularRecipes()
}