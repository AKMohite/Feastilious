package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.SimilarRecipeEntity

@Dao
interface SimilarRecipeDAO: BaseDAO<SimilarRecipeEntity> {

    @Query("DELETE FROM similar_recipes WHERE parentRecipeId = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)

    @Query("SELECT COUNT(*) FROM similar_recipes WHERE parentRecipeId = :id")
    suspend fun getCountForRecipe(id: Long): Int
}