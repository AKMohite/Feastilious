package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity

@Dao
interface SimilarRecipeDAO: BaseDAO<SimilarRecipeEntity> {

    @Query("DELETE FROM similar_recipes WHERE parentRecipeId = :recipeId")
    fun deleteRecipe(recipeId: Long)
}