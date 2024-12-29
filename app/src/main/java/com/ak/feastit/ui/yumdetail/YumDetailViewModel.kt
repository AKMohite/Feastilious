package com.ak.feastit.ui.yumdetail

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARGS_RECIPE_ID = "recipeId"

@HiltViewModel
internal class YumDetailViewModel @Inject constructor(
    private val repository: RecipeRepository,
    dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(
    dispatcher
) {

    init {
        val id = savedStateHandle.get<Long>(ARGS_RECIPE_ID) ?: throw IllegalArgumentException("Recipe id is required")
        refreshRecipeInfo(id)
    }

    override fun handleError(exception: Throwable) {
        super.handleError(exception)
    }

    private fun refreshRecipeInfo(recipeId: Long, forceRefresh: Boolean = false) {
        uiScope.launch {
            repository.refreshRecipe(id = recipeId, forceRefresh = forceRefresh)
            repository.refreshAnalyzedInstruction(id = recipeId, forceRefresh = forceRefresh)
            repository.refreshSimilarRecipes(id = recipeId, forceRefresh = forceRefresh)
        }
    }

    fun load() {

    }
}