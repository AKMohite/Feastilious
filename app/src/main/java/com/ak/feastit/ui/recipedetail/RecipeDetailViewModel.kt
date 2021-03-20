package com.ak.feastit.ui.recipedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.feastit.domain.recipedetails.RecipeDetailUseCase
import com.ak.feastit.domain.recipelist.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
        private val detailUseCase: RecipeDetailUseCase
) : ViewModel() {

    fun getRecipe(id: Long) {
        detailUseCase.execute(id)
                .onEach { detailState ->
                    detailState.data?.let { detail ->

                    }
                }
                .launchIn(viewModelScope)
    }


}