package com.ak.feastit.ui.yumdetail.tabs.overview.component

import com.mak.feastit.domain.model.Recipe

internal sealed interface RecipeOverviewItem {
    fun areContentsSame(other: RecipeOverviewItem): Boolean
    fun areItemsSame(other: RecipeOverviewItem): Boolean

    data class Heading(val text: String) : RecipeOverviewItem {
        override fun areContentsSame(other: RecipeOverviewItem): Boolean {
            return (other as? Heading)?.text == text
        }

        override fun areItemsSame(other: RecipeOverviewItem): Boolean {
            return other == this
        }
    }
    data class Text(val value: String) : RecipeOverviewItem {

        override fun areContentsSame(other: RecipeOverviewItem): Boolean {
            return (other as? Text)?.value == value
        }

        override fun areItemsSame(other: RecipeOverviewItem): Boolean {
            return other == this
        }
    }
    data class Recipes(val items: List<Recipe>): RecipeOverviewItem {

        override fun areContentsSame(other: RecipeOverviewItem): Boolean {
            return (other as? Recipes)?.items == items
        }

        override fun areItemsSame(other: RecipeOverviewItem): Boolean {
            return other == this
        }
    }
}