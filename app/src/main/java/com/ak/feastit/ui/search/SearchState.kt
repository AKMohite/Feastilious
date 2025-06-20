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
}

internal enum class ImageSearch {
    CAMERA,
    GALLERY
}