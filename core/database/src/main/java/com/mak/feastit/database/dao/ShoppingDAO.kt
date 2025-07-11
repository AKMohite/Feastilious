// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mak.feastit.database.entity.ShoppingEntity
import com.mak.feastit.database.entity.custom.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDAO : BaseDAO<ShoppingEntity> {
  @Query("SELECT * FROM shopping_ingredients WHERE recipe_id =:recipeId")
  suspend fun getCart(recipeId: Long): List<ShoppingEntity>

  @Query("DELETE FROM shopping_ingredients WHERE recipe_id =:recipeId")
  suspend fun deleteCart(recipeId: Long)

  @Query("SELECT * FROM shopping_ingredients WHERE id =:ingredientId")
  suspend fun getIngredient(ingredientId: String): ShoppingEntity?

  @Query("SELECT * FROM shopping_ingredients WHERE recipe_id =:recipeId")
  fun observeCartForRecipe(recipeId: Long): Flow<List<ShoppingEntity>>

  @Query(
    "SELECT cart.id, cart.is_bought AS isBought, ing.aisle_category AS aisleCategory, " +
      "ing.ingredient_name AS ingredientName, ing.recipe_id AS recipeId, ing.quantity, " +
      "ing.unit, r.name AS recipeName, r.servings, r.img AS recipeImg FROM " +
      "shopping_ingredients cart INNER JOIN recipe_ingredients ing ON cart.id = ing.id " +
      "INNER JOIN recipes r ON ing.recipe_id = r.id",
  )
  fun observeCart(): Flow<List<CartEntity>>

  @Query("DELETE FROM shopping_ingredients WHERE recipe_id =:recipeId")
  suspend fun removeRecipeFromCart(recipeId: Long)

  @Query("SELECT recipe_id FROM shopping_ingredients")
  suspend fun getAllIds(): List<Long>

//    TODO check
//    @Query("SELECT * FROM shopping_ingredients")
//    fun getEmbeddedCart(): Flow<RecipeWithIngredient>
}
