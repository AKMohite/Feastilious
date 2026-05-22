// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.cart

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.compose.base.BaseViewModel
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SAVED_STATE_CART_ORDER_BY = "saved-cart-order-by"

@HiltViewModel
internal class ShoppingCartViewModel
@Inject
constructor(
  private val repository: CartRepository,
  private val dispatcher: DispatcherProvider,
  private val savedState: SavedStateHandle,
) : BaseViewModel(dispatcher) {
  private val _state = MutableStateFlow(CartState())
  val state = _state.asStateFlow()

  private val orderBy = savedState.getStateFlow(SAVED_STATE_CART_ORDER_BY, CartOrderBy.RECIPE)

  init {
    observeCart()
  }

  fun removeRecipe(recipeId: Long) {
    uiScope.launch {
      repository.removeRecipeFromCart(recipeId)
    }
  }

  fun toggleIngredient(ingredientId: String) {
    uiScope.launch {
      repository.toggleCartIngredient(ingredientId)
    }
  }

  fun removeIngredient(ingredientId: String) {
    uiScope.launch {
      repository.deleteCartIngredient(ingredientId)
    }
  }

  fun setCartOrderBy(orderBy: CartOrderBy) {
    Timber.d("Set cart order by $orderBy")
    savedState[SAVED_STATE_CART_ORDER_BY] = orderBy
  }

  private fun observeCart() {
    combine(orderBy, repository.observerCart()) { orderBy, ingredients ->
      ingredientToCart(orderBy, ingredients)
    }.flowOn(dispatcher.computation)
      .onEach { cartItems ->
        _state.update { currentState -> currentState.copy(cart = cartItems) }
      }.launchIn(uiScope)
  }

  private fun ingredientToCart(
    orderBy: CartOrderBy,
    ingredients: List<CartIngredient>,
  ): List<ShoppingCart> {
    val cart = mutableListOf<ShoppingCart>()
    when (orderBy) {
      CartOrderBy.AISLE -> {
        val aisleIngredients = ingredients.groupBy { it.aisleCategory.lowercase() }

        for (group in aisleIngredients) {
          val cartIngredient = group.value[0]
          cart.add(ShoppingCart.AisleCategory(cartIngredient.aisleCategory))
          val cartIngredients =
            group.value
              .sortedBy { ingredient ->
                ingredient.id
              }.sortedBy { ingredient -> ingredient.isBought }
              .map { ShoppingCart.Ingredient(it) }
          cart.addAll(cartIngredients)
        }
      }

      CartOrderBy.RECIPE -> {
        val recipeIngredients = ingredients.groupBy { it.recipeId }
        for (group in recipeIngredients) {
          val cartIngredient = group.value[0]
          val recipe =
            CartRecipe(
              id = group.key,
              name = cartIngredient.recipeName,
              img = cartIngredient.recipeImg,
              servings = cartIngredient.servings,
            )
          cart.add(ShoppingCart.RecipeHeading(recipe))
          val allIngredients = group.value.sortedBy { ingredient -> ingredient.isBought }
            .map { ShoppingCart.Ingredient(it) }
          cart.addAll(allIngredients)
        }
      }
    }
    return cart.toList()
  }
}

internal enum class CartOrderBy {
  AISLE,
  RECIPE,
}
