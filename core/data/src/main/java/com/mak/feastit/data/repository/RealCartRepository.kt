package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDetailMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.model.Shopping
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class RealCartRepository @Inject constructor(
    private val db: FeastDB,
    private val mapper: RecipeDetailMapper,
    private val dispatcher: DispatcherProvider
): CartRepository {

    override fun observerCart(): Flow<List<CartIngredient>> {
        Timber.d("Observe shopping cart")
        return db.shoppingDAO().observeCart()
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                mapper.entitiesToCart(entities)
            }.flowOn(dispatcher.computation)
    }

    override suspend fun removeRecipeFromCart(recipeId: Long) = withContext(dispatcher.io) {
        Timber.d("Delete recipe $recipeId from cart")
        db.shoppingDAO().removeRecipeFromCart(recipeId)
    }

    override suspend fun toggleCartIngredient(ingredientId: String) = withContext(dispatcher.io) {
        val ingredient = db.shoppingDAO().getIngredient(ingredientId) ?: return@withContext
        val toggle = !ingredient.isBought
        Timber.d("Toggle ingredient: ${ingredient.id} with value: $toggle")
        db.shoppingDAO().update(ingredient.copy(isBought = toggle))
    }

    override suspend fun deleteCartIngredient(ingredientId: String) = withContext(dispatcher.io) {
        val ingredient = db.shoppingDAO().getIngredient(ingredientId) ?: return@withContext
        Timber.d("Delete cart ingredient: ${ingredient.id}")
        db.shoppingDAO().delete(ingredient)
    }

    override suspend fun toggleShoppingIngredientsForRecipe(recipeId: Long) = withContext(dispatcher.io) {
        val shopping = db.shoppingDAO().getCart(recipeId)
        if (shopping.isEmpty()) {
            Timber.d("Adding ingredients to cart for recipe: $recipeId")
//            add to shopping
            val ingredients = db.ingredientDAO().getIngredientsFor(recipeId).firstOrNull() ?: return@withContext
            val shoppingCart = mapper.ingredientsToShoppingCarts(ingredients)
            db.shoppingDAO().insert(shoppingCart)
        } else {
            Timber.d("Remove ingredients from cart for recipe: $recipeId")
            db.shoppingDAO().deleteCart(recipeId)
        }
    }


    override suspend fun toggleShoppingIngredient(ingredientId: String) = withContext(dispatcher.io) {
        val shopping = db.shoppingDAO().getIngredient(ingredientId)
        if (shopping == null) {
            Timber.d("Add ingredient to shopping cart: $ingredientId")
//            add to shopping
            val ingredient = db.ingredientDAO().getIngredient(ingredientId) ?: return@withContext
            val shoppingCart = mapper.ingredientToShoppingCart(ingredient)
            db.shoppingDAO().insert(shoppingCart)
        } else {
            Timber.d("Remove ingredient from shopping cart: $ingredientId")
            db.shoppingDAO().delete(shopping)
        }
    }

    override fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>> {
        return db.shoppingDAO().observeCartForRecipe(recipeId)
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities ->
                mapper.entityToShopping(entities)
            }.flowOn(dispatcher.computation)
    }
}