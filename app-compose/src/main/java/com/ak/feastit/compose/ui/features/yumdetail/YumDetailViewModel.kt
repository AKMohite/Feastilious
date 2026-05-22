// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.yumdetail

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.compose.R
import com.ak.feastit.compose.base.BaseViewModel
import com.ak.feastit.compose.worker.WorkerScheduler
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.MealPlanRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.usecase.RecipeDetailIngredientsUsecase
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

internal const val SAVED_RECIPE_ID = "recipe_id"

@HiltViewModel
internal class YumDetailViewModel
@Inject
constructor(
  private val recipeRepository: RecipeRepository,
  private val cartRepository: CartRepository,
  private val mealPlanRepository: MealPlanRepository,
  private val getIngredients: RecipeDetailIngredientsUsecase,
  private val scheduler: WorkerScheduler,
  private val dispatcher: DispatcherProvider,
  savedStateHandle: SavedStateHandle,
) : BaseViewModel(
  dispatcher,
) {
  private val _state = MutableStateFlow(YumDetailState())
  val state = _state.asStateFlow()
  private var recipeId: Long

  init {
    val id = savedStateHandle.get<Long>(SAVED_RECIPE_ID)
      ?: throw IllegalArgumentException("Recipe id is required")
    recipeId = id
    refreshRecipeInfo(id)
    observeRecipe(id)
  }

  fun toggleMealPlan() {
    uiScope.launch {
      val recipeMealPlans = mealPlanRepository.getMealPlansForRecipe(recipeId)
      val isAdded = mealPlanRepository.toggleMealPLanFor(recipeId)
      withContext(dispatcher.computation) {
        if (!isAdded) {
          val scheduledMeals = recipeMealPlans.filter { meal -> meal.scheduledFor != null }
          for (meal in scheduledMeals) {
            scheduler.cancelMealWorker(meal.id)
          }
        }
      }
    }
  }

  private fun refreshRecipeInfo(
    recipeId: Long,
    forceRefresh: Boolean = false,
  ) {
    uiScope.launch {
      recipeRepository.refreshRecipe(id = recipeId, forceRefresh = forceRefresh)
      recipeRepository.refreshAnalyzedInstruction(id = recipeId, forceRefresh = forceRefresh)
      recipeRepository.refreshSimilarRecipes(id = recipeId, forceRefresh = forceRefresh)
    }
  }

  private fun observeRecipe(id: Long) {
    val base =
      combine(
        recipeRepository.observerRecipe(id),
        recipeRepository.observerSimilarRecipes(id),
        getRecipeIngredients(id),
        getRecipeSteps(id),
        recipeRepository.observeNutrients(id),
      ) { recipe, similarRecipes, ingredients, instructions, nutrients ->
        DetailAggregate(recipe, similarRecipes, ingredients, instructions, nutrients)
      }

    combine(base, mealPlanRepository.hasRecipe(id)) { aggregate, isInMealPlan ->
      _state.update { currentState ->
        currentState.copy(
          overview = aggregate.recipe,
          similarRecipes = aggregate.similarRecipes,
          ingredientSections = aggregate.ingredientSections,
          instructions = getRecipeStepSections(aggregate.instructions, isInMealPlan),
          nutrients = aggregate.nutrients,
        )
      }
    }.launchIn(uiScope)
  }

  private fun getRecipeSteps(id: Long) = recipeRepository
    .observeInstructions(id)
    .map { instructions ->
      if (instructions.isEmpty()) return@map emptyList()
      val stepSections = mutableListOf<StepSection>()
      val steps =
        instructions.map { instruction ->
          StepSection.Item(instruction)
        }
      stepSections.addAll(steps)
      stepSections.toList()
    }.flowOn(dispatcher.computation)

  private fun getRecipeIngredients(id: Long) = getIngredients(id)
    .map { ingredients ->
      if (ingredients.isEmpty()) return@map emptyList()
      val isInCart = ingredients.any { ingredient -> ingredient.isInCart }
      val headerTitle =
        if (isInCart) {
          R.string.remove_ingredients_from_shopping_list
        } else {
          R.string.add_ingredients_to_shopping_list
        }
      val ingredientsSections = mutableListOf<IngredientSection>()
      val headerSection = IngredientSection.Header(title = headerTitle)
      ingredientsSections.add(headerSection)
      val ingredientSection = ingredients.map { IngredientSection.Item(it) }
      ingredientsSections.addAll(ingredientSection)
      ingredientsSections.toList()
    }.flowOn(dispatcher.computation)

  fun toggleFavorite() {
    uiScope.launch {
      val id = state.value.overview?.recipeId ?: return@launch
      recipeRepository.toggleFavorite(id)
    }
  }

  fun toggleIngredientCart(id: String) {
    uiScope.launch {
      cartRepository.toggleShoppingIngredient(id)
    }
  }

  fun toggleCart() {
    uiScope.launch {
      cartRepository.toggleShoppingAllIngredientsForRecipe(recipeId)
    }
  }

  private fun getRecipeStepSections(
    sections: List<StepSection>,
    isInMealPlan: Boolean,
  ): List<StepSection> {
    val mutable = sections.toMutableList()
    val headerTitle =
      if (isInMealPlan) {
        R.string.recipe_remove_from_meal_plan
      } else {
        R.string.recipe_add_to_meal_plan
      }
    val headerSection = StepSection.Header(title = headerTitle)
    mutable.add(0, headerSection)
    return mutable.toList()
  }
}

private data class DetailAggregate(
  val recipe: RecipeDetail,
  val similarRecipes: List<Recipe>,
  val ingredientSections: List<IngredientSection>,
  val instructions: List<StepSection>,
  val nutrients: List<com.mak.feastit.domain.model.Nutrient>,
)
