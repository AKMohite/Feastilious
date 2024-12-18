package com.ak.feastit.ui.explore

import com.mak.feastit.domain.model.Recipe

internal sealed interface ExploreAdapterItem {
    val category: ExploreCategory
    val items: List<Any>
    fun hasNestedChildren(): Boolean = items.isNotEmpty()

    data class TopBanner(
        override val category: ExploreCategory = ExploreCategory.BANNER_RECIPES,
        override val items: List<Recipe>,
    ): ExploreAdapterItem
//    data class SectionHeader(val category: ExploreCategory, val hasMore: Boolean): ExploreAdapterItem
    data class HorizontalChips(
        override val category: ExploreCategory,
        override val items: List<ExploreChip>
    ): ExploreAdapterItem
    data class HorizontalRecipes(
        override val category: ExploreCategory,
        override val items: List<Recipe>,
        val isLoading: Boolean
    ): ExploreAdapterItem
}

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
