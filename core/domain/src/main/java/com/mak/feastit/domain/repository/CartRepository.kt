package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.CartIngredient
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun observerCart(): Flow<List<CartIngredient>>
    suspend fun removeRecipeFromCart(recipeId: Long)
    suspend fun toggleCartIngredient(ingredientId: String)
    suspend fun deleteCartIngredient(ingredientId: String)
}