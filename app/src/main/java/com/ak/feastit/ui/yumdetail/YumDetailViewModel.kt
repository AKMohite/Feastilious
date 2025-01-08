package com.ak.feastit.ui.yumdetail

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.usecase.RecipeDetailIngredientsUsecase
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val ARGS_RECIPE_ID = "recipeId"

@HiltViewModel
internal class YumDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val cartRepository: CartRepository,
    private val ingredients: RecipeDetailIngredientsUsecase,
    dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(
    dispatcher
) {

    private val _state = MutableStateFlow(YumDetailState())
    val state = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<Long>(ARGS_RECIPE_ID) ?: throw IllegalArgumentException("Recipe id is required")
        refreshRecipeInfo(id)
        observeRecipe(id)
    }

    override fun handleError(exception: Throwable) {
        super.handleError(exception)
    }

    private fun refreshRecipeInfo(recipeId: Long, forceRefresh: Boolean = false) {
        uiScope.launch {
            Timber.d("Refresh recipe info for: $recipeId")
            recipeRepository.refreshRecipe(id = recipeId, forceRefresh = forceRefresh)
            recipeRepository.refreshAnalyzedInstruction(id = recipeId, forceRefresh = forceRefresh)
            recipeRepository.refreshSimilarRecipes(id = recipeId, forceRefresh = forceRefresh)
        }
    }

    private fun observeRecipe(id: Long) {
        Timber.d("Observe recipe details: $id")
        combine(
            recipeRepository.observerRecipe(id),
            recipeRepository.observerSimilarRecipes(id),
            ingredients(id),
            recipeRepository.observeInstructions(id)
        ) { recipe, similarRecipes, ingredients, instructions ->
            _state.update { currentState ->
                currentState.copy(
                    overview = recipe,
                    similarRecipes = similarRecipes,
                    ingredients = ingredients,
                    instructions = instructions
                )
            }
        }.launchIn(uiScope)
    }

    fun toggleFavorite() {
        uiScope.launch {
            val id = state.value.overview?.recipeId ?: return@launch
            Timber.d("Toggle favorite: $id")
            recipeRepository.toggleFavorite(id)
        }
    }

    fun toggleIngredientCart(id: String) {
        uiScope.launch {
            Timber.d("Toggle ingredient for shopping: $id")
            cartRepository.toggleShoppingIngredient(id)
        }
    }

}

data class YumDetailState(
    val overview: RecipeDetail? = null,
    val similarRecipes: List<Recipe> = emptyList(),
    val instructions: List<Instruction> = emptyList(),
    val ingredients: List<Ingredient> = emptyList()
) {
    val areIngredientsInCart = ingredients.any { ingredient ->
        ingredient.isInCart
    }
}
