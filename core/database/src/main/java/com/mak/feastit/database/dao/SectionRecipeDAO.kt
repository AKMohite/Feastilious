package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mak.feastit.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionRecipeDAO<Entity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<Entity>)

    fun getRecipes(page: Int): Flow<List<RecipeEntity>>

    suspend fun deletePage(page: Int)

    suspend fun deleteRecipes()
}