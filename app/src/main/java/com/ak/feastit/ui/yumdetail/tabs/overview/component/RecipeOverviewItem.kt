// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.overview.component

import com.mak.feastit.domain.model.Recipe

internal sealed interface RecipeOverviewItem {
  fun areContentsSame(other: RecipeOverviewItem): Boolean

  fun areItemsSame(other: RecipeOverviewItem): Boolean

  data class Heading(
    val text: String,
  ) : RecipeOverviewItem {
    override fun areContentsSame(other: RecipeOverviewItem): Boolean = (other as? Heading)?.text == text

    override fun areItemsSame(other: RecipeOverviewItem): Boolean = other == this
  }

  data class Text(
    val value: String,
  ) : RecipeOverviewItem {
    override fun areContentsSame(other: RecipeOverviewItem): Boolean = (other as? Text)?.value == value

    override fun areItemsSame(other: RecipeOverviewItem): Boolean = other == this
  }

  data class Recipes(
    val items: List<Recipe>,
  ) : RecipeOverviewItem {
    override fun areContentsSame(other: RecipeOverviewItem): Boolean = (other as? Recipes)?.items == items

    override fun areItemsSame(other: RecipeOverviewItem): Boolean = other == this
  }
}
