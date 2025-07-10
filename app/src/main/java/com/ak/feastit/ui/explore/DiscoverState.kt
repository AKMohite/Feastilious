package com.ak.feastit.ui.explore

import com.mak.feastit.domain.model.Recipe

internal data class DiscoverState(
    // this is loading state of whole screen
    val isLoading: Boolean = false,
    private val sections: List<ExploreSection> = emptyList()
) {
    fun displayableSections(): List<ExploreAdapterItem> {
        val exploreSections = exploreSections()
        return exploreSections.toAdapterItems()
    }

    private fun exploreSections() = sections
        .filter { section -> section.row != null && section.row.contents.isNotEmpty() }
        .sortedBy { section -> section.category.ordinal }

    private fun List<ExploreSection>.toAdapterItems(): List<ExploreAdapterItem> {
        val mutableList = mutableListOf<ExploreAdapterItem>()
        val sections = this.toMutableList()
        this.firstOrNull { section ->
            section.category == ExploreCategory.BANNER_RECIPES && section.row?.contents?.isNotEmpty() == true
        }?.let { exploreSection ->
            val items = (exploreSection.row as? ExploreRow.RecipeRows)?.contents ?: return@let
            mutableList.add(ExploreAdapterItem.TopBanner(items = items))
            sections.removeIf { section ->  section.category == ExploreCategory.BANNER_RECIPES }
        }
        sections.forEach { section ->
            when(section.row) {
                is ExploreRow.Chips -> mutableList.add(ExploreAdapterItem.HorizontalChips(section.category, section.row.contents))
                is ExploreRow.RecipeRows -> mutableList.add(ExploreAdapterItem.HorizontalRecipes(section.category, section.row.contents, section.isLoading))
                null -> Unit
            }
        }
        return mutableList.sortedBy { item -> item.category.ordinal }
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
        return exploreSections().none { section ->
            ExploreCategory.staticCategoryElements().any { it == section.category && section.row?.contents?.isEmpty() == true }.not()
        }
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
    val name: String,
    val type: ExploreCategory
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

// TODO check [ExploreAdapterItem] to have multi view type items directly
internal data class ExploreSection(
    // this is loading state of each section in screen
    val isLoading: Boolean,
    val category: ExploreCategory,
    val row: ExploreRow<*>? = null // TODO why we need to pass any *, need to check other
) {
    val hasMoreItems: Boolean = row is ExploreRow.RecipeRows
}