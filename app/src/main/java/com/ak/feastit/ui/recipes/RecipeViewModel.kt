package com.ak.feastit.ui.recipes

import androidx.lifecycle.*
import com.ak.domain.category.GetCategoriesUseCase
import com.ak.domain.category.RecipeCategory
import com.ak.domain.utils.RecipeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val useCase: GetCategoriesUseCase,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _allCategories = MutableLiveData<RecipeResult<List<RecipeCategory>>>()
    val allCategories: LiveData<RecipeResult<List<RecipeCategory>>>
        = _allCategories
    init {
        viewModelScope.launch {
            _allCategories.value = useCase.execute(Unit)
        }
    }
}