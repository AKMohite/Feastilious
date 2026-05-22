// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.favorites

import androidx.annotation.StringRes
import com.mak.feastit.domain.model.Recipe

internal data class FavoriteState(
  val recipes: List<Recipe> = emptyList(),
  val suggestions: List<Suggestion> = emptyList(),
)

internal sealed interface Suggestion {
  fun areItemsTheSame(other: Suggestion): Boolean

  fun areContentsTheSame(other: Suggestion): Boolean

  data class Heading(
    @StringRes val title: Int,
  ) : Suggestion {
    override fun areItemsTheSame(other: Suggestion): Boolean = other is Heading

    override fun areContentsTheSame(other: Suggestion): Boolean = title == (other as? Heading)?.title
  }

  data class Item(
    val suggestions: Recipe,
  ) : Suggestion {
    override fun areItemsTheSame(other: Suggestion): Boolean = other is Item

    override fun areContentsTheSame(other: Suggestion): Boolean = suggestions.id == (other as? Item)?.suggestions?.id
  }

  data class Text(
    val value: String,
    val type: SuggestionType,
  ) : Suggestion {
    override fun areItemsTheSame(other: Suggestion): Boolean = other is Text

    override fun areContentsTheSame(other: Suggestion): Boolean = value == (other as? Text)?.value
  }
}

internal enum class SuggestionType {
  RECENT_SEARCH,
  DB_TEXT,
}
