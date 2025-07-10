package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.mak.feastit.database.entity.NutrientEntity

@Dao
interface NutrientDAO: BaseDAO<NutrientEntity> {

    @Query("DELETE FROM recipe_nutrients WHERE recipe_id = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)
}