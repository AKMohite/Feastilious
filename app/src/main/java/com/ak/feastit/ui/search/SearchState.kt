package com.ak.feastit.ui.search

import com.mak.feastit.domain.model.Recipe

internal data class SearchState(
    val isLoading: Boolean = false,
    val recentSearches: List<String> = emptyList(), // TODO map ui components in viewmodel and not in getSuggestions()
    val recommendations: List<Recipe> = emptyList(), // TODO map ui components in viewmodel and not in getSuggestions()
    val searchResults: List<Recipe> = emptyList()
) {
    fun hasRecentSearches(): Boolean {
        return (recentSearches.isNotEmpty() || recommendations.isNotEmpty()) && searchResults.isEmpty()
    }

    fun getSuggestions(
        recentsHeader: String,
        recommendationsHeader: String
    ): List<SearchSuggestionItem> {
        val suggestions = mutableListOf<SearchSuggestionItem>()
        if (recentSearches.isNotEmpty()) {
            suggestions.add(SearchSuggestionItem.Header(recentsHeader))
            recentSearches.forEach { query ->
                suggestions.add(SearchSuggestionItem.History(query))
            }
        }
        if (recommendations.isNotEmpty()) {
            suggestions.add(SearchSuggestionItem.Header(recommendationsHeader))
            recommendations.forEach { recipe ->
                suggestions.add(SearchSuggestionItem.Recommendation(recipe))
            }
        }
        return suggestions.toList()
    }
}

internal sealed interface SearchAction {
    data class OnImageClick(val type: ImageSearch) : SearchAction
    data object OnBackPress : SearchAction
}

internal enum class ImageSearch {
    CAMERA,
    GALLERY
}

/*

listOf(
filter(
val name: String,
val filterItems: List<>
)
)



sortBy -> ascending and descending
data class AscDesc(
val type: FilterType,
val name: String,
val isAsc: Boolean? = null
)
chip flow with single selection
data class Single(
val type: FilterType,
val name: String
val isChecked: Boolean
)
* */



internal enum class SortBy {
    POPULAR,
    TOP_RATED,
    HEALTHY,
    QUICK,
    POCKET_FRIENDLY
}

internal enum class FilterType {
    SORT,
    CUISINE,
    MEAL,
    DIET,
    INTOLERANCES
}

// Represents a single selectable item within a filter category group
internal data class FilterGroupItem(
    val id: String, // Unique identifier for this option
    val displayName: String,
    val type: FilterType,
    val isAscending: Boolean? = null,
    val isSelected: Boolean = false // For multi-select or to indicate current selection
)

internal enum class SelectionMode {
    SINGLE, // Only one option can be selected (e.g., for Sort)
    MULTI   // Multiple options can be selected (e.g., for Cuisines, Intolerances)
}

// A sealed interface to represent the different states or types of filters
// This can be useful if different filter types need drastically different data structures
// For now, FilterGroup might be sufficient, but this is an option for more complex scenarios.
internal sealed interface SearchFilter {
    val type: FilterType
    val displayName: String // e.g., "Sort By", "Cuisines"

    data class SingleSelectFilter(
        override val type: FilterType,
        override val displayName: String,
        val options: List<FilterGroupItem>,
        val selectedOption: FilterGroupItem? = null // Store the currently selected one
    ) : SearchFilter

    data class MultiSelectFilter(
        override val type: FilterType,
        override val displayName: String,
        val options: List<FilterGroupItem> // isSelected within FilterOption will manage selections
    ) : SearchFilter
}