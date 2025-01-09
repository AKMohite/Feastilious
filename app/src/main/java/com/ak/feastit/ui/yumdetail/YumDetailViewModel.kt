package com.ak.feastit.ui.yumdetail

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.R
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val ARGS_RECIPE_ID = "recipeId"

@HiltViewModel
internal class YumDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val cartRepository: CartRepository,
    private val getIngredients: RecipeDetailIngredientsUsecase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(
    dispatcher
) {

    private val _state = MutableStateFlow(YumDetailState())
    val state = _state.asStateFlow()
    private var recipeId: Long? = null

    init {
        val id = savedStateHandle.get<Long>(ARGS_RECIPE_ID) ?: throw IllegalArgumentException("Recipe id is required")
        recipeId = id
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
            getRecipeIngredients(id),
            getRecipeSteps(id)
        ) { recipe, similarRecipes, ingredients, instructions ->
            _state.update { currentState ->
                currentState.copy(
                    overview = recipe,
                    similarRecipes = similarRecipes,
                    ingredientSections = ingredients,
                    instructions = instructions
                )
            }
        }.launchIn(uiScope)
    }

    private fun getRecipeSteps(id: Long) = recipeRepository.observeInstructions(id)
        .map { instructions ->
            if (instructions.isEmpty()) return@map emptyList()
            val stepSections = mutableListOf<StepSection>()
//            TODO check if recipe is added to meal plan
            val headerTitle = R.string.recipe_add_to_meal_plan
            val headerSection = StepSection.Header(title = headerTitle)
            stepSections.add(headerSection)
            val steps = instructions.map { instruction ->
                StepSection.Item(instruction)
            }
            stepSections.addAll(steps)
            stepSections.toList()
        }.flowOn(dispatcher.computation)

    private fun getRecipeIngredients(id: Long) = getIngredients(id)
        .map { ingredients ->
            if (ingredients.isEmpty()) return@map emptyList()
            val isInCart = ingredients.any { ingredient -> ingredient.isInCart }
            val headerTitle = if (isInCart) {
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

    fun toggleCart() {
        uiScope.launch {
            cartRepository.toggleShoppingAllIngredientsForRecipe(recipeId ?: return@launch)
        }
    }

}

internal data class YumDetailState(
    val overview: RecipeDetail? = null,
    val similarRecipes: List<Recipe> = emptyList(),
    val instructions: List<StepSection> = emptyList(),
    val ingredientSections: List<IngredientSection> = emptyList()
) {
    val areIngredientsInCart = ingredientSections.filterIsInstance<IngredientSection.Item>()
        .any { section -> section.ingredient.isInCart }
}


internal sealed interface IngredientSection {
    fun areItemsTheSame(newItem: IngredientSection): Boolean
    fun areContentsTheSame(newItem: IngredientSection): Boolean

    data class Header(@StringRes val title: Int) : IngredientSection {
        override fun areItemsTheSame(newItem: IngredientSection): Boolean {
            return newItem is Header
        }

        override fun areContentsTheSame(newItem: IngredientSection): Boolean {
            val section = newItem as? Header ?: return false
            return title == section.title
        }
    }

    data class Item(val ingredient: Ingredient) : IngredientSection {
        override fun areItemsTheSame(newItem: IngredientSection): Boolean {
            return newItem is Item && ingredient.id == newItem.ingredient.id
        }

        override fun areContentsTheSame(newItem: IngredientSection): Boolean {
            val item = (newItem as? Item)?.ingredient ?: return false
            return ingredient.isSameAs(item)
        }
    }
}

internal sealed interface StepSection {
    fun areItemsTheSame(newItem: StepSection): Boolean
    fun areContentsTheSame(newItem: StepSection): Boolean

    data class Header(@StringRes val title: Int) : StepSection {
        override fun areItemsTheSame(newItem: StepSection): Boolean {
            return newItem is Header
        }

        override fun areContentsTheSame(newItem: StepSection): Boolean {
            val section = newItem as? Header ?: return false
            return title == section.title
        }
    }

    data class Item(val instruction: Instruction) : StepSection {
        override fun areItemsTheSame(newItem: StepSection): Boolean {
            return newItem is Item
        }

        override fun areContentsTheSame(newItem: StepSection): Boolean {
            val item = (newItem as? Item)?.instruction ?: return false
            return instruction.isSameAs(item)
        }
    }
}