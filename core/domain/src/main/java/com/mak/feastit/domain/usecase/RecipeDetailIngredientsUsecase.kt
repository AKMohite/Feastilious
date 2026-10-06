// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.usecase

import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

class RecipeDetailIngredientsUsecase
@Inject
constructor(
  private val recipeRepository: RecipeRepository,
  private val cartRepository: CartRepository,
  private val dispatcher: DispatcherProvider,
) : ObserveUsecase<Long, Flow<List<Ingredient>>>() {
  override fun invoke(params: Long): Flow<List<Ingredient>> = combine(
    recipeRepository.observeIngredients(params),
    cartRepository.observeShoppingCartForRecipe(params),
  ) { ingredients, shoppings ->
    val cartIngredients = shoppings.associateBy { it.id }
    ingredients.map { ingredient ->
      cartIngredients[ingredient.id]?.let { shopping ->
        ingredient.copy(isInCart = true, isBought = shopping.isBought)
      } ?: ingredient
    }
  }.flowOn(dispatcher.computation)
}
