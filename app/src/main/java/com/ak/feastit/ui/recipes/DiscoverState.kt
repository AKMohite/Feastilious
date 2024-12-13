package com.ak.feastit.ui.recipes

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

    fun isEmpty(): Boolean {
        return displayableSections().isEmpty()
    }
}

internal enum class ExploreCategory {
    BANNER_RECIPES,
    POPULAR_RECIPES,
    MEAL_TYPE_CHIPS,
    TOP_RATED_RECIPES,
    CUISINE_TYPE_CHIPS,
    HEALTHY_RECIPES,
    DIET_TYPE_CHIPS,
    QUICK_RECIPES,
    POCKET_FRIENDLY_RECIPES
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
)