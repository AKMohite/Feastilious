package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipeDetailMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.model.CartIngredient
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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
}