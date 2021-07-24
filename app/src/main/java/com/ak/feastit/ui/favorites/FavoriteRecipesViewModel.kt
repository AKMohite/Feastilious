package com.ak.feastit.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.domain.usecase.FavRecipeUseCase
import com.ak.feastit.domain.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
        private val favRecipeUseCase: FavRecipeUseCase
) : ViewModel() {

    val favRecipes = MutableStateFlow<List<Recipe>>(emptyList())

    init {
        favSearchRecipe("")
    }

    fun favSearchRecipe(searchQuery: String) {
        favRecipeUseCase.execute(searchQuery)
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        favRecipes.value = recipes
                    }
                }
                .launchIn(viewModelScope)
    }

}