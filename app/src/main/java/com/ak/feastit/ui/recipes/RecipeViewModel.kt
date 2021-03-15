package com.ak.feastit.ui.recipes

import androidx.lifecycle.*
import com.ak.feastit.data.utils.FeastPrefManager
import com.ak.feastit.domain.category.GetCategoriesUseCase
import com.ak.feastit.domain.category.RecipeCategory
import com.ak.feastit.domain.recipelist.RandomRecipeUseCase
import com.ak.feastit.domain.recipelist.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val randomRecipeUseCase: RandomRecipeUseCase,
        val prefManager: FeastPrefManager,
        val savedStateHandle: SavedStateHandle
): ViewModel() {

    val allCategories = MutableStateFlow<List<RecipeCategory>>(emptyList())
    val dashboardRecipes = MutableStateFlow<List<Recipe>>(emptyList())

    init {
        getCategoriesUseCase.execute(Unit).onEach { state ->
            state.data?.let { categories ->
                allCategories.value = categories
            }
        }.launchIn(viewModelScope)

        randomRecipeUseCase.execute(Unit).onEach { state ->
            state.data?.let { recipes ->
                dashboardRecipes.value = recipes
            }
        }.launchIn(viewModelScope)
    }
}