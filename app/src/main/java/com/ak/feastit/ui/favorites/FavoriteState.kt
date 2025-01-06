package com.ak.feastit.ui.favorites

import androidx.annotation.StringRes
import com.mak.feastit.domain.model.Recipe

internal data class FavoriteState(
    val recipes: List<Recipe> = emptyList(),
    val suggestions: List<Suggestion> = emptyList()
)

internal sealed interface Suggestion {
    fun areItemsTheSame(other: Suggestion): Boolean
    fun areContentsTheSame(other: Suggestion): Boolean

    data class Heading(@StringRes val title: Int): Suggestion {
        override fun areItemsTheSame(other: Suggestion): Boolean {
            return other is Heading
        }

        override fun areContentsTheSame(other: Suggestion): Boolean {
            return title == (other as? Heading)?.title
        }
    }

    data class Item(val suggestions: Recipe): Suggestion {
        override fun areItemsTheSame(other: Suggestion): Boolean {
            return other is Item
        }

        override fun areContentsTheSame(other: Suggestion): Boolean {
            return suggestions.id == (other as? Item)?.suggestions?.id
        }
    }

    data class Text(val value: String, val type: SuggestionType): Suggestion {
        override fun areItemsTheSame(other: Suggestion): Boolean {
            return other is Text
        }

        override fun areContentsTheSame(other: Suggestion): Boolean {
            return value == (other as? Text)?.value
        }
    }
}

internal enum class SuggestionType {
    RECENT_SEARCH,
    DB_TEXT
}