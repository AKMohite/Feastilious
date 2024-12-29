package com.ak.feastit.ui.legacyrecipedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.usecase.LegacyRecipeDetailUseCase
import com.mak.feastit.domain.usecase.LegacyToggleFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LegacyRecipeDetailViewModel @Inject constructor(
    private val detailUseCase: LegacyRecipeDetailUseCase,
    private val toggleFavUseCase: LegacyToggleFavUseCase
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