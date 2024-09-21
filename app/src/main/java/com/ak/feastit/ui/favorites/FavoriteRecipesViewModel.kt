package com.ak.feastit.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.usecase.FavRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
        favRecipeUseCase(searchQuery)
                .onEach {  recipeResult ->
                    recipeResult.data?.let { recipes ->
                        favRecipes.update { recipes }
                    }
                }
                .launchIn(viewModelScope)
    }

}