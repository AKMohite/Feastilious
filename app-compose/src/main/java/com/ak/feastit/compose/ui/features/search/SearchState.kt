// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.search

import com.mak.feastit.domain.model.Recipe

internal data class SearchState(
  val isLoading: Boolean = false,
  val recentSearches: List<String> = emptyList(),
  val recommendations: List<Recipe> = emptyList(),
  val searchResults: List<Recipe> = emptyList(),
) {
  fun hasRecentSearches(): Boolean {
    return (recentSearches.isNotEmpty() || recommendations.isNotEmpty()) && searchResults.isEmpty()
  }
}

internal sealed interface SearchAction {
  data class OnImageClick(val type: ImageSearch, val fileName: String) : SearchAction
  data object OnBackPress : SearchAction
}

internal enum class ImageSearch {
  CAMERA,
  GALLERY,
}

internal enum class SortBy {
  POPULAR,
  TOP_RATED,
  HEALTHY,
  QUICK,
  POCKET_FRIENDLY,
}

internal enum class FilterType {
  SORT,
  CUISINE,
  MEAL,
  DIET,
  INTOLERANCES,
}

internal sealed interface FilterGroupItem {
  data class SortingGroup(
    val id: String,
    val type: FilterType,
    val isAscending: Boolean? = null,
  ) : FilterGroupItem

  data class SingleSelectionGroup(
    val id: String,
    val name: String,
    val type: FilterType,
    val isSelected: Boolean = false,
  ) : FilterGroupItem
}

internal enum class SelectionMode {
  SINGLE,
  MULTI,
}

internal sealed interface SearchFilter {
  val type: FilterType

  data class SingleSelectFilter(
    override val type: FilterType,
    val options: List<FilterGroupItem>,
  ) : SearchFilter

  data class MultiSelectFilter(
    override val type: FilterType,
    val options: List<FilterGroupItem>,
  ) : SearchFilter
}
