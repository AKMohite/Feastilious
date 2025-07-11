// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.Shopping
import kotlinx.coroutines.flow.Flow

interface CartRepository {
  /**
   * Observe all shopping ingredients in cart
   */
  fun observerCart(): Flow<List<CartIngredient>>

  /**
   * Remove all ingredients of recipe from cart
   */
  suspend fun removeRecipeFromCart(recipeId: Long)

  /**
   * Toggle ingredient in cart whether it is bought or not
   */
  suspend fun toggleCartIngredient(ingredientId: String)

  /**
   * Delete ingredient from cart
   */
  suspend fun deleteCartIngredient(ingredientId: String)

  /**
   * Observe shopping ingredients in cart for recipe
   */
  fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>>

  /**
   * Toggle shopping ingredients for recipe to add or remove all recipe ingredients from cart
   */
  suspend fun toggleCartIngredientsForRecipe(recipeId: Long)

  /**
   * Toggle ingredient in recipe whether to add or remove from cart
   */
  suspend fun toggleShoppingIngredient(ingredientId: String)

  /**
   * Toggle shopping ingredients for recipe to add or remove all recipe ingredients from cart
   */
  suspend fun toggleShoppingAllIngredientsForRecipe(recipeId: Long)
}
