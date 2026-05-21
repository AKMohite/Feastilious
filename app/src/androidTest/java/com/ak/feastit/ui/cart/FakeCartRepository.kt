// Copyright 2026, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.cart

import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.Shopping
import com.mak.feastit.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCartRepository : CartRepository {
  private val cartFlow = MutableStateFlow<List<CartIngredient>>(emptyList())

  fun emit(ingredients: List<CartIngredient>) {
    cartFlow.value = ingredients
  }

  override fun observerCart(): Flow<List<CartIngredient>> = cartFlow

  override suspend fun removeRecipeFromCart(recipeId: Long) {
    cartFlow.value = cartFlow.value.filter { it.recipeId != recipeId }
  }

  override suspend fun toggleCartIngredient(ingredientId: String) {
    cartFlow.value = cartFlow.value.map {
      if (it.id == ingredientId) it.copy(isBought = !it.isBought) else it
    }
  }

  override suspend fun deleteCartIngredient(ingredientId: String) {
    cartFlow.value = cartFlow.value.filter { it.id != ingredientId }
  }

  override fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>> {
    return cartFlow.map { list ->
      list.filter { it.recipeId == recipeId }.map { Shopping(it.id, it.isBought) }
    }
  }

  override suspend fun toggleCartIngredientsForRecipe(recipeId: Long) {}

  override suspend fun toggleShoppingIngredient(ingredientId: String) {}

  override suspend fun toggleShoppingAllIngredientsForRecipe(recipeId: Long) {}
}
