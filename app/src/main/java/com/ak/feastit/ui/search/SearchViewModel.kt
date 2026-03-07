// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.data.utils.FeastPrefManager
import com.mak.feastit.domain.model.Cuisine
import com.mak.feastit.domain.model.DietType
import com.mak.feastit.domain.model.Intolerances
import com.mak.feastit.domain.model.RecipeMealType
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SAVED_SEARCH_QUERY = "saved-search-query"
private const val SAVED_IMAGE_PATH = "saved-image-path"

@HiltViewModel
internal class SearchViewModel
@Inject
constructor(
  private val prefManager: FeastPrefManager,
  private val repository: RecipesRepository,
  private val savedStateHandle: SavedStateHandle,
  private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider) {
  private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
  val state: Flow<SearchState> = _state
  private val _filters: MutableStateFlow<List<SearchFilter>> = MutableStateFlow(emptyList())
  val filters: Flow<List<SearchFilter>> = _filters

  private val _action: Channel<SearchAction> = Channel()
  val action = _action.receiveAsFlow()

  init {
    combine(
      repository.getRecipes(SyncType.POPULAR_RECIPES, 1),
      prefManager.preferencesFlow.map {
        it.recentSearches
      },
    ) { recipes, recentSearches ->
      Timber.d("Recent searches: ${recentSearches.count()}")
      Timber.d("Recipes: ${recipes.count()}")
      val searchHistory = recentSearches.map { it.trim() }.filter { it.isNotBlank() }
      _state.update { current ->
        current.copy(
          recentSearches = searchHistory,
          recommendations = recipes.shuffled(),
        )
      }
    }.launchIn(uiScope)
    observeSearchedQuery()
  }

  private fun observeSearchedQuery() {
//        savedStateHandle.getStateFlow(SAVED_SEARCH_QUERY, "")
//            .filterNot { query -> query.isBlank() }
//            .flatMapLatest {
//                repository.observeSearchSuggestions(it)
//            }.cach
  }

  fun onImageClick(type: ImageSearch) {
    uiScope.launch {
      _action.send(SearchAction.OnImageClick(type, "need to have file name here maybe"))
    }
  }

  fun search(query: String) {
    uiScope.launch {
      prefManager.addRecentSearches(query)
      savedStateHandle[SAVED_SEARCH_QUERY] = query
//            TODO make API call with filters or get from local DB?
      val results = repository.searchRecipe(query)
      _state.update { current -> current.copy(searchResults = results) }
    }
  }

  fun clearSearch() {
    uiScope.launch {
      _state.update { currentState -> currentState.copy(searchResults = emptyList()) }
    }
  }

  fun onBackPress() {
    uiScope.launch {
      val state = state.firstOrNull() ?: return@launch
      if (state.searchResults.isNotEmpty()) {
        clearSearch()
      } else {
        _action.send(SearchAction.OnBackPress)
      }
    }
  }

  fun loadFilters() {
    uiScope.launch(dispatcherProvider.computation) {
      if (filters.firstOrNull()?.isNotEmpty() == true) return@launch
      Timber.d("Load filters")
      val searchFilters = mutableListOf<SearchFilter>()
      val sortFilter =
        SortBy.entries.map {
          FilterGroupItem.SortingGroup(it.name, FilterType.SORT)
        }
      searchFilters.add(SearchFilter.SingleSelectFilter(FilterType.SORT, sortFilter))
      val cuisine =
        Cuisine.entries.map {
          FilterGroupItem.SingleSelectionGroup(it.name, it.title, FilterType.CUISINE)
        }
      searchFilters.add(SearchFilter.SingleSelectFilter(FilterType.CUISINE, cuisine))
      val meal =
        RecipeMealType.entries.map {
          FilterGroupItem.SingleSelectionGroup(it.name, it.name, FilterType.MEAL)
        }
      searchFilters.add(SearchFilter.SingleSelectFilter(FilterType.MEAL, meal))
      val diet =
        DietType.entries.map {
          FilterGroupItem.SingleSelectionGroup(it.name, it.title, FilterType.DIET)
        }
      searchFilters.add(SearchFilter.SingleSelectFilter(FilterType.DIET, diet))
      val intolerances =
        Intolerances.entries.map {
          FilterGroupItem.SingleSelectionGroup(it.name, it.name, FilterType.INTOLERANCES)
        }
      searchFilters.add(
        SearchFilter.SingleSelectFilter(
          FilterType.INTOLERANCES,
          intolerances,
        ),
      )
      _filters.update { searchFilters }
    }
  }

  fun onFilterChanged(
    option: FilterGroupItem,
    isChecked: Boolean,
  ) {
    uiScope.launch {
      Timber.d("$option is $isChecked")
    }
  }

  fun imageCaptured() {
    uploadAndFindRecipe()
  }

  fun setImageUri(photoUri: String?) {
    if (photoUri.isNullOrBlank()) return
    Timber.d("The image uri path is: $photoUri")
    savedStateHandle[SAVED_IMAGE_PATH] = photoUri
  }

  fun onGalleryImageSelected(path: String?) {
    if (path.isNullOrBlank()) return
    setImageUri(path)
    uploadAndFindRecipe()
  }

  private fun uploadAndFindRecipe() {
    val imagePath = savedStateHandle.get<String?>(SAVED_IMAGE_PATH).orEmpty()
    if (imagePath.isBlank()) return
    uiScope.launch {
      _state.update { it.copy(isLoading = true) }
      val recipes = repository.searchRecipeByImage(imagePath)
      _state.update { current ->
        current.copy(
          recommendations = recipes.shuffled(),
        )
      }
    }.invokeOnCompletion {
      _state.update { it.copy(isLoading = false) }
    }
  }
}
