package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity

@Dao
interface RecipeStepDAO: BaseDAO<RecipeStepEntity> {

    @Query("DELETE FROM recipe_instructions WHERE recipe_id = :recipeId")
    fun deleteRecipe(recipeId: Long)
}