package com.ak.feastit.ui.cart

import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Nutrient
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeRecipeRepository : RecipeRepository {
  override suspend fun refreshRecipe(id: Long, forceRefresh: Boolean) {}

  override suspend fun refreshAnalyzedInstruction(id: Long, forceRefresh: Boolean) {}

  override suspend fun refreshSimilarRecipes(id: Long, forceRefresh: Boolean) {}

  override suspend fun toggleFavorite(recipeId: Long) {}

  override fun observerRecipe(id: Long): Flow<RecipeDetail> = emptyFlow()

  override fun observerSimilarRecipes(id: Long): Flow<List<Recipe>> = emptyFlow()

  override fun observeIngredients(id: Long): Flow<List<Ingredient>> = emptyFlow()

  override fun observeInstructions(id: Long): Flow<List<Instruction>> = emptyFlow()

  override fun observeNutrients(id: Long): Flow<List<Nutrient>> = emptyFlow()
}
