// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.explore

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.compose.base.BaseViewModel
import com.ak.feastit.compose.utils.getEnumTitle
import com.mak.feastit.domain.model.Cuisine
import com.mak.feastit.domain.model.DietType
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeMealType
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class ExploreViewModel
@Inject
constructor(
  private val recipesRepository: RecipesRepository,
  private val dispatcher: DispatcherProvider,
  savedStateHandle: SavedStateHandle,
) : BaseViewModel(dispatcher) {
  private val selectedCategory =
    savedStateHandle.getStateFlow<String?>(
      "selectedCategory",
      initialValue = null,
    )

  private val _state = MutableStateFlow(DiscoverState())
  val state = _state.asStateFlow()

  init {
    initCategories()
    observeCategories()
    refreshCategories(false)
  }

  private fun initCategories() {
    val sections =
      ExploreCategory.entries.map { category ->
        ExploreSection(
          isLoading = false,
          category = category,
        )
      }
    val newState =
      DiscoverState(
        isLoading = false,
        sections = sections,
      )
    _state.update { newState }
  }

  private fun refreshCategories(forceRefresh: Boolean = false) {
    uiScope
      .launch {
        _state.update { it.refreshSections(true) }
        Timber.d("Refresh explore sections")
        ExploreCategory
          .getRefreshExploreEntries()
          .map { category ->
            async { refreshCategory(category, forceRefresh) }
          }.awaitAll()
      }.invokeOnCompletion {
        Timber.d("Explore sections refreshed")
        _state.update { discoverState ->
          discoverState.refreshSections(isLoading = false)
          discoverState.setError(it?.message)
        }
      }
  }

  private suspend fun refreshCategory(
    category: ExploreCategory,
    forceRefresh: Boolean = false,
  ) {
    recipesRepository.refreshRecipes(category.toSyncType(), 1, forceRefresh)
  }

  private fun observeCategories() {
    Timber.d("Observe sections for explore")
    combine(
      flows = ExploreCategory.entries.map(::observeCategory),
      transform = { it.toList() },
    ).onStart {
      _state.update { it.loading(true) }
    }
      .onEach { sections ->
        _state.update { it.copy(sections = sections) }
      }.onCompletion {
        _state.update { it.loading(false) }
      }.catch { cause ->
        handleError(cause)
      }.launchIn(uiScope)
  }

  private fun observeCategory(category: ExploreCategory): Flow<ExploreSection> = when {
    category == ExploreCategory.BANNER_RECIPES ->
      recipesRepository
        .getRecipes(SyncType.POPULAR_RECIPES, 1)
        .map { recipes ->
          recipeToExploreSection(recipes.shuffled().take(6), category)
        }.flowOn(dispatcher.computation)

    category == ExploreCategory.MEAL_TYPE_CHIPS ->
      flowOf(
        ExploreSection(
          isLoading = false,
          category = category,
          row = ExploreRow.Chips(RecipeMealType.entries.toMealExploreChips()),
        ),
      )

    category == ExploreCategory.CUISINE_TYPE_CHIPS ->
      flowOf(
        ExploreSection(
          isLoading = false,
          category = category,
          row = ExploreRow.Chips(Cuisine.entries.toCuisineExploreChips()),
        ),
      )

    category == ExploreCategory.DIET_TYPE_CHIPS ->
      flowOf(
        ExploreSection(
          isLoading = false,
          category = category,
          row = ExploreRow.Chips(DietType.entries.toDietExploreChips()),
        ),
      )

    ExploreCategory.observerExploreEntries().any { it == category } ->
      recipesRepository
        .getRecipes(category.toSyncType(), 1)
        .map { recipes ->
          recipeToExploreSection(recipes, category)
        }.flowOn(dispatcher.computation)

    else -> throw IllegalArgumentException("$category cannot be observed")
  }

  private fun recipeToExploreSection(
    recipes: List<Recipe>,
    category: ExploreCategory,
  ): ExploreSection = ExploreSection(
    isLoading = false,
    category = category,
    row = ExploreRow.RecipeRows(recipes),
  )
}

private fun List<DietType>.toDietExploreChips(): List<ExploreChip> = this.map { dietType ->
  ExploreChip(
    id = dietType.ordinal + 1,
    title = dietType.title,
    name = dietType.name,
    type = ExploreCategory.DIET_TYPE_CHIPS,
  )
}

private fun List<Cuisine>.toCuisineExploreChips(): List<ExploreChip> = this.map { cuisine ->
  ExploreChip(
    id = cuisine.ordinal + 1,
    title = cuisine.title,
    name = cuisine.name,
    type = ExploreCategory.CUISINE_TYPE_CHIPS,
  )
}

private fun List<RecipeMealType>.toMealExploreChips(): List<ExploreChip> = this.map { mealType ->
  ExploreChip(
    id = mealType.ordinal + 1,
    title = mealType.name.getEnumTitle(),
    name = mealType.name,
    type = ExploreCategory.MEAL_TYPE_CHIPS,
  )
}
