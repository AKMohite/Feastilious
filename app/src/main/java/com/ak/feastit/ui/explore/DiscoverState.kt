package com.ak.feastit.ui.explore

import com.mak.feastit.domain.model.Recipe

internal data class DiscoverState(
    // this is loading state of whole screen
    val isLoading: Boolean = false,
    private val sections: List<ExploreSection> = emptyList()
) {
    fun displayableSections(): List<ExploreSection> {
        return sections
            .filter { section -> section.row != null && section.row.contents.isNotEmpty() }
            .sortedBy { section -> section.category.ordinal }
    }

    fun loading(isLoading: Boolean = false): DiscoverState {
        return this.copy(isLoading = isLoading)
    }

    fun sectionLoading(category: ExploreCategory, isLoading: Boolean): DiscoverState {
        val currentSections = this.sections
        val updatedSections = currentSections.map { section ->
            if (section.category == category) {
                section.copy(isLoading = isLoading)
            } else {
                section
            }
        }
        return this.copy(sections = updatedSections)
    }

    fun isEmpty(): Boolean {
        return displayableSections().isEmpty()
    }

    fun refreshSections(isLoading: Boolean): DiscoverState {
        val currentSections = this.sections
        val updatedSections = currentSections.map { section ->
            section.copy(isLoading = isLoading)
        }
        return this.copy(sections = updatedSections)
    }
}

internal data class ExploreChip(
    val id: Int,
//    @StringRes
//    val titleId: Int,
    val title: String,
    val name: String
)

internal sealed interface ExploreRow<T> {
    val contents: List<T>
    data class RecipeRows(
        override val contents: List<Recipe>
    ): ExploreRow<Recipe>
    data class Chips(
        override val contents: List<ExploreChip>
    ): ExploreRow<ExploreChip>
}

internal data class ExploreSection(
    // this is loading state of each section in screen
    val isLoading: Boolean,
    val category: ExploreCategory,
    val row: ExploreRow<*>? = null // TODO why we need to pass any *, need to check other
) {
    val hasMoreItems: Boolean = row is ExploreRow.RecipeRows
}