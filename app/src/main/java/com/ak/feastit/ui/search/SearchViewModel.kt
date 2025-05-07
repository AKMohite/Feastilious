package com.ak.feastit.ui.search

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.data.utils.FeastPrefManager
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    prefManager: FeastPrefManager,
    private val repository: RecipesRepository,
    private val savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    init {
        combine(repository.getRecipes(SyncType.POPULAR_RECIPES, 1), prefManager.preferencesFlow.map { it.recentSearches }) { recipes, recentSearches ->
            Timber.d("Recent searches: $recentSearches")
            Timber.d("Recipes: $recipes")
        }.launchIn(uiScope)
    }

    fun reload() {}

}