package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.RecipeStepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeStepDAO: BaseDAO<RecipeStepEntity> {

    @Query("DELETE FROM recipe_instructions WHERE recipe_id = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)

    @Query("SELECT COUNT(*) FROM recipe_instructions WHERE recipe_id = :id LIMIT 1")
    suspend fun getCountForRecipe(id: Long): Int

    @Query("SELECT * FROM recipe_instructions WHERE recipe_id = :id")
    fun getStepsFor(id: Long): Flow<List<RecipeStepEntity>>

}