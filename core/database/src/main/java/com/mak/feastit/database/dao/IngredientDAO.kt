package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.IngredientEntity

@Dao
interface IngredientDAO: BaseDAO<IngredientEntity> {

    @Query("DELETE FROM recipe_ingredients WHERE recipe_id = :recipeId")
    fun deleteRecipe(recipeId: Long)
}