package com.ak.feastit.ui.explore

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.ui.recipes.DiscoverState
import com.ak.feastit.ui.recipes.ExploreCategory
import com.ak.feastit.ui.recipes.ExploreChip
import com.ak.feastit.ui.recipes.ExploreRow
import com.ak.feastit.ui.recipes.ExploreSection
import com.mak.feastit.domain.model.Cuisine
import com.mak.feastit.domain.model.DietType
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeMealType
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import javax.inject.Inject


@HiltViewModel
internal class ExploreViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val selectedCategory = savedStateHandle.getStateFlow<String?>(
        "selectedCategory",
        initialValue = null
    )


    private val _state = MutableStateFlow(DiscoverState())
    val state = _state.asStateFlow()

    init {

        initCategories()

        observeCategories()
//        TODO: maybe force refresh in initialisation
        refreshCategories(false)
    }

    private fun initCategories() {
        val sections = ExploreCategory.entries.map { category ->
            ExploreSection(
                isLoading = false,
                category = category
            )
        }
        val newState = DiscoverState(
            isLoading = false,
            sections = sections
        )
        _state.update { newState }
    }

    override fun handleError(exception: Throwable) {
        super.handleError(exception)
    }

    private fun refreshCategories(forceRefresh: Boolean = false) {
        uiScope.launch {
            recipesRepository.refreshRecipes(SyncType.POPULAR_RECIPES, 1, forceRefresh)
            recipesRepository.refreshRecipes(SyncType.TOP_RATED_RECIPES, 1)
//            recipesRepository.refreshRecipes(SyncType.HEALTHY_RECIPES, 1)
//            recipesRepository.refreshRecipes(SyncType.QUICK_RECIPES, 1)
//            recipesRepository.refreshRecipes(SyncType.POCKET_FRIENDLY_RECIPES, 1)
//            TODO use supervisor
//            refresh popular
//            refresh top rated
//            refresh healthy
//            refresh quick
//            refresh pocket friendly
        }
    }

    private fun observeCategories() {
        combine(
            flows = ExploreCategory.entries.map(::observeCategory),
            transform = { it.toList() }
        )
            .onStart {
                _state.update { it.copy(isLoading = true) }
            }
//            .filter {  } // maybe filter out empty sections here instead of state
            .onEach { sections ->
                val newState = DiscoverState(
                    isLoading = false,
                    sections = sections
                )
                _state.update { newState }
            }
            .onCompletion {
                _state.update { it.copy(isLoading = false) }
            }
            .launchIn(uiScope)
    }

    private fun observeCategory(category: ExploreCategory): Flow<ExploreSection> {
        val section: Flow<ExploreSection> = when(category) {
            ExploreCategory.BANNER_RECIPES -> recipesRepository.getRecipes(SyncType.POPULAR_RECIPES, 1)
                .map { recipes ->
                    recipeToExploreSection(recipes, category)
                }
                .flowOn(dispatcher.computation)
            ExploreCategory.POPULAR_RECIPES -> recipesRepository.getRecipes(SyncType.POPULAR_RECIPES, 1)
                .map { recipes ->
                    recipeToExploreSection(recipes, category)
                }
                .flowOn(dispatcher.computation)
            ExploreCategory.MEAL_TYPE_CHIPS -> {
                flowOf(
                    ExploreSection(
                    isLoading = false,
                    category = category,
                    row = ExploreRow.Chips(RecipeMealType.entries.toMealExploreChips())
                )
                )
            }
            ExploreCategory.TOP_RATED_RECIPES -> recipesRepository.getRecipes(SyncType.TOP_RATED_RECIPES, 1)
                .map { recipes ->
                    recipeToExploreSection(recipes, category)
                }
                .flowOn(dispatcher.computation)
            ExploreCategory.CUISINE_TYPE_CHIPS -> {
                flowOf(
                    ExploreSection(
                    isLoading = false,
                    category = category,
                    row = ExploreRow.Chips(Cuisine.entries.toCuisineExploreChips())
                )
                )
            }
//            ExploreCategory.HEALTHY_RECIPES -> TODO()
            ExploreCategory.DIET_TYPE_CHIPS -> {
                flowOf(
                    ExploreSection(
                    isLoading = false,
                    category = category,
                    row = ExploreRow.Chips(DietType.entries.toDietExploreChips())
                )
                )
            }
//            ExploreCategory.QUICK_RECIPES -> TODO()
//            ExploreCategory.POCKET_FRIENDLY_RECIPES -> TODO()
            else -> {
//                todo remove else block after implementing all sections
                flowOf(
                    ExploreSection(
                    isLoading = false,
                    category = category,
                    row = ExploreRow.RecipeRows(listOf(
                        Recipe(1L, "", "")
                    ))
                )
                )
            }
        }
        return section
    }

    private fun recipeToExploreSection(recipes: List<Recipe>, category: ExploreCategory): ExploreSection {
        return ExploreSection(
            isLoading = false,
            category = category,
            row = ExploreRow.RecipeRows(recipes))
    }
}

private fun List<DietType>.toDietExploreChips(): List<ExploreChip> {
    return this.map { dietType ->
        ExploreChip(
            id = dietType.ordinal + 1, // just to not have 0 as chip id
            title = dietType.name,
            name = dietType.name
        )
    }
}

private fun List<Cuisine>.toCuisineExploreChips(): List<ExploreChip> {
    return this.map { cuisine ->
        ExploreChip(
            id = cuisine.ordinal + 1, // just to not have 0 as chip id
            title = cuisine.name,
            name = cuisine.name
        )
    }
}


private fun List<RecipeMealType>.toMealExploreChips(): List<ExploreChip> {
    return this.map { mealType ->
        ExploreChip(
            id = mealType.ordinal + 1, // just to not have 0 as chip id
            title = mealType.name,
            name = mealType.name
        )
    }
}
