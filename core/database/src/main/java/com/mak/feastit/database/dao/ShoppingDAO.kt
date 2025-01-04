package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.ShoppingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDAO: BaseDAO<ShoppingEntity> {

    @Query("SELECT * FROM shopping_ingredients WHERE recipe_id =:recipeId")
    suspend fun getCart(recipeId: Long): List<ShoppingEntity>

    @Query("DELETE FROM shopping_ingredients WHERE recipe_id =:recipeId")
    suspend fun deleteCart(recipeId: Long)

    @Query("SELECT * FROM shopping_ingredients WHERE id =:ingredientId")
    suspend fun getIngredient(ingredientId: String): ShoppingEntity?
}