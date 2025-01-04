package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.Shopping
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun refreshRecipe(id: Long, forceRefresh: Boolean = false)
    suspend fun refreshAnalyzedInstruction(id: Long, forceRefresh: Boolean)
    suspend fun refreshSimilarRecipes(id: Long, forceRefresh: Boolean)
    suspend fun toggleFavorite(recipeId: Long)
    suspend fun toggleShoppingIngredients(recipeId: Long)
    suspend fun toggleShoppingIngredient(ingredientId: String)
    fun observerRecipe(id: Long): Flow<RecipeDetail>
    fun observerSimilarRecipes(id: Long): Flow<List<Recipe>>
    fun observeIngredients(id: Long): Flow<List<Ingredient>>
    fun observeShoppingCartForRecipe(recipeId: Long): Flow<List<Shopping>>
    fun observeInstructions(id: Long): Flow<List<Instruction>>
}
