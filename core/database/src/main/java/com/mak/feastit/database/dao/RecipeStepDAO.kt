package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.RecipeStepEntity

@Dao
interface RecipeStepDAO: BaseDAO<RecipeStepEntity> {

    @Query("DELETE FROM recipe_instructions WHERE recipe_id = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)

    @Query("SELECT COUNT(*) FROM recipe_instructions WHERE recipe_id = :id LIMIT 1")
    suspend fun getCountForRecipe(id: Long): Int
}