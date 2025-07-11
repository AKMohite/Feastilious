// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search

import com.mak.feastit.domain.model.Recipe

sealed interface SearchSuggestionItem {
  fun areItemsSame(other: SearchSuggestionItem): Boolean

  fun areContentsSame(other: SearchSuggestionItem): Boolean

  data class Header(
    val name: String,
  ) : SearchSuggestionItem {
    override fun areItemsSame(other: SearchSuggestionItem): Boolean = other is Header && other == this

    override fun areContentsSame(other: SearchSuggestionItem): Boolean {
      val item = other as? Header ?: return false
      return item.name == name
    }
  }

  data class History(
    val result: String,
  ) : SearchSuggestionItem {
    override fun areItemsSame(other: SearchSuggestionItem): Boolean = other is History && other == this

    override fun areContentsSame(other: SearchSuggestionItem): Boolean {
      val item = other as? History ?: return false
      return item.result == result
    }
  }

  data class Recommendation(
    val recipe: Recipe,
  ) : SearchSuggestionItem {
    override fun areItemsSame(other: SearchSuggestionItem): Boolean = other is Recommendation && other == this

    override fun areContentsSame(other: SearchSuggestionItem): Boolean {
      val item = other as? Recommendation ?: return false
      return item.recipe.isSameAs(other.recipe)
    }
  }
}
