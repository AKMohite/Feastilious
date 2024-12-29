package com.ak.feastit.ui.yumdetail

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.repository.IRecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val ARGS_RECIPE_ID = "recipeId"

@HiltViewModel
internal class YumDetailViewModel @Inject constructor(
    private val repository: IRecipeRepository,
    private val dispatcher: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(
    dispatcher
) {

    init {
        val id = savedStateHandle.get<Long>(ARGS_RECIPE_ID) ?: throw IllegalArgumentException("Recipe id is required")
    }
}