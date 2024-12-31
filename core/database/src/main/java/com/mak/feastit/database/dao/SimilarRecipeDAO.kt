package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SimilarRecipeDAO: BaseDAO<SimilarRecipeEntity> {

    @Query("DELETE FROM similar_recipes WHERE parentRecipeId = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)

    @Query("SELECT COUNT(*) FROM similar_recipes WHERE parentRecipeId = :id")
    suspend fun getCountForRecipe(id: Long): Int

    @Query("SELECT r.* FROM similar_recipes s INNER JOIN recipes r ON s.recipeId = r.id WHERE s.parentRecipeId = :id")
    fun getRecipes(id: Long): Flow<List<RecipeEntity>>
}