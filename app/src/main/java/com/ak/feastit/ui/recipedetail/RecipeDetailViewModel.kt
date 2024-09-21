package com.ak.feastit.ui.recipedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.feastit.data.model.RecipeDetail
import com.mak.feastit.data.usecase.RecipeDetailUseCase
import com.mak.feastit.data.usecase.ToggleFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val detailUseCase: RecipeDetailUseCase,
    private val toggleFavUseCase: ToggleFavUseCase
) : ViewModel() {
    val recipeDetail: MutableStateFlow<RecipeDetail?> = MutableStateFlow(null)
    val favRecipe: MutableStateFlow<Boolean> = MutableStateFlow(false)
    fun getRecipe(id: Long) {
        detailUseCase(id)
                .onEach { detailState ->
                    detailState.data?.let { detail ->
                        recipeDetail.value = detail
                        favRecipe.value = detail.isAdded
                    }
                }
                .launchIn(viewModelScope)
    }

    fun toggleFav() {
        recipeDetail.value?.let { recipe ->
            toggleFavUseCase(recipe.recipeId, !favRecipe.value)
                    .onEach { favState ->
                        favState.data?.let { fav ->
                            if (fav) {
                                favRecipe.value = !favRecipe.value
                            }
                        }
                    }.launchIn(viewModelScope)
        }
    }


}