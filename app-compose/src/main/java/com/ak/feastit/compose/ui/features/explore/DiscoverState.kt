// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.explore

import com.mak.feastit.domain.model.Recipe

internal data class DiscoverState(
  // this is loading state of whole screen
  val isLoading: Boolean = false,
  val errorMessage: String? = null,
  val sections: List<ExploreSection> = emptyList(),
) {
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

  fun refreshSections(isLoading: Boolean): DiscoverState {
    val currentSections = this.sections
    val updatedSections = currentSections.map { section ->
      section.copy(isLoading = isLoading)
    }
    return this.copy(sections = updatedSections)
  }

  fun setError(message: String?): DiscoverState {
    return this.copy(errorMessage = message)
  }
}

internal data class ExploreChip(
  val id: Int,
  val title: String,
  val name: String,
  val type: ExploreCategory,
)

internal sealed interface ExploreRow<T> {
  val contents: List<T>

  data class RecipeRows(
    override val contents: List<Recipe>,
  ) : ExploreRow<Recipe>

  data class Chips(
    override val contents: List<ExploreChip>,
  ) : ExploreRow<ExploreChip>
}

internal data class ExploreSection(
  // this is loading state of each section in screen
  val isLoading: Boolean,
  val category: ExploreCategory,
  val row: ExploreRow<*>? = null,
) {
  val hasMoreItems: Boolean = row is ExploreRow.RecipeRows
}
