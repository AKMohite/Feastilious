// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.favorites

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.compose.R
import com.ak.feastit.compose.base.BaseViewModel
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

private const val STATE_SEARCH_SUGGESTIONS = "saved-search-suggestions"

@HiltViewModel
internal class FavoriteViewModel
@Inject
constructor(
  private val dispatcher: DispatcherProvider,
  private val repository: RecipesRepository,
  private val savedState: SavedStateHandle,
) : BaseViewModel(dispatcher) {
  private val _state = MutableStateFlow(FavoriteState())
  val state = _state.asStateFlow()

  init {
    observeFavoriteRecipes()
    observerSearchSuggestions()
  }

  fun onQueryChanged(query: String) {
    savedState[STATE_SEARCH_SUGGESTIONS] = query
  }

  private fun observerSearchSuggestions() {
    combine(getUserSearch(), getRecentSearches()) { userSearch, recentSearch ->
      recentSearch + userSearch
    }.map { suggestions ->
      _state.update { currentState -> currentState.copy(suggestions = suggestions) }
    }.launchIn(uiScope)
  }

  private fun getRecentSearches(): Flow<List<Suggestion>> {
    val suggestions = mutableListOf<Suggestion>()
//                TODO get recents from data store
    suggestions.add(Suggestion.Heading(R.string.search_recipe_recent_searches))
    suggestions.add(Suggestion.Text("Chicken", SuggestionType.RECENT_SEARCH))
    suggestions.add(Suggestion.Text("Ramen", SuggestionType.RECENT_SEARCH))
    suggestions.add(Suggestion.Text("Soup", SuggestionType.RECENT_SEARCH))
    suggestions.add(Suggestion.Text("Burger", SuggestionType.RECENT_SEARCH))
    return flowOf(suggestions)
  }

  private fun getUserSearch(): Flow<List<Suggestion>> = savedState
    .getStateFlow(STATE_SEARCH_SUGGESTIONS, "")
    .debounce(300L)
    .distinctUntilChanged()
    .filter { query -> query.isNotBlank() }
    .flatMapMerge { query ->
      repository.observeSearchSuggestions(query)
    }.map { recipes ->
      val suggestions = mutableListOf<Suggestion>()
      val recipeNames =
        recipes.take(10).map { Suggestion.Text(it.name, SuggestionType.DB_TEXT) }
      suggestions.addAll(recipeNames)
//                this is search suggestion with recipes
      suggestions.add(Suggestion.Heading(R.string.search_recipe_suggestions))
      suggestions.addAll(recipes.map { Suggestion.Item(it) })
      suggestions
    }.flowOn(dispatcher.computation)

  private fun observeFavoriteRecipes() {
    repository
      .observeFavoriteRecipes()
      .onEach { recipes ->
        _state.update { currentState -> currentState.copy(recipes = recipes) }
      }.launchIn(uiScope)
  }
}
