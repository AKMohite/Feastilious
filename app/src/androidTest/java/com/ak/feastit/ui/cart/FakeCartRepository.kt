package com.ak.feastit.ui.cart

import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.Shopping
import com.mak.feastit.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCartRepository : CartRepository {
  private val _cartFlow = MutableStateFlow<List<CartIngredient>>(emptyList())

  fun emit(ingredients: List<CartIngredient>) {
    _cartFlow.value = ingredients
  }

  override fun observerCart(): Flow<List<CartIngredient>> = _cartFlow

  override suspend fun removeRecipeFromCart(recipeId: Long) {
    _cartFlow.value = _cartFlow.value.filter { it.recipeId != recipeId }
  }

  override suspend fun toggleCartIngredient(ingredientId: String) {
    _cartFlow.value = _cartFlow.value.map {
      if (it.id == ingredientId) it.copy(isBought = !it.isBought) else it
    }
  }

  override suspend fun deleteCartIngredient(ingredientId: String) {
    _cartFlow.value = _cartFlow.value.filter { it.id != ingredientId }
  }

  override fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>> {
    return _cartFlow.map { list ->
      list.filter { it.recipeId == recipeId }.map { Shopping(it.id, it.isBought) }
    }
  }

  override suspend fun toggleCartIngredientsForRecipe(recipeId: Long) {}

  override suspend fun toggleShoppingIngredient(ingredientId: String) {}

  override suspend fun toggleShoppingAllIngredientsForRecipe(recipeId: Long) {}
}
