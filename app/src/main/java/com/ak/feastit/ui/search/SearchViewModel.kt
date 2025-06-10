package com.ak.feastit.ui.search

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.data.utils.FeastPrefManager
import com.ak.feastit.ui.mealplanner.MealPlanAction
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    prefManager: FeastPrefManager,
    private val repository: RecipesRepository,
    private val savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private val _action: Channel<SearchAction> = Channel()
    val action = _action.receiveAsFlow()

    init {
        combine(repository.getRecipes(SyncType.POPULAR_RECIPES, 1), prefManager.preferencesFlow.map { it.recentSearches }) { recipes, recentSearches ->
            Timber.d("Recent searches: $recentSearches")
            Timber.d("Recipes: $recipes")
        }.launchIn(uiScope)
    }

    fun reload() {}
    fun onImageClick(type: ImageSearch) {
        uiScope.launch {
            _action.send(SearchAction.OnImageClick(type))
        }
    }

}

internal sealed interface SearchAction {
    data class OnImageClick(val type: ImageSearch) : SearchAction
}

internal enum class ImageSearch {
    CAMERA,
    GALLERY
}