package com.ak.feastit.ui.recipes

import androidx.lifecycle.*
import com.ak.feastit.domain.category.GetCategoriesUseCase
import com.ak.feastit.domain.category.RecipeCategory
import com.ak.feastit.domain.utils.RecipeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    useCase: GetCategoriesUseCase,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val allCategories = MutableStateFlow<List<RecipeCategory>>(emptyList())

    init {
            useCase.execute(Unit).onEach { state ->
                state.data?.let { categories ->
                    allCategories.value = categories
                }
            }.launchIn(viewModelScope)
    }
}