// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.favorites.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentRecipeBinding
import com.ak.feastit.databinding.ComponentSearchSuggestionHeadingBinding
import com.ak.feastit.databinding.ComponentSearchSuggestionTextBinding
import com.ak.feastit.ui.favorites.Suggestion

private const val SUGGESTION_TYPE_HEADING = 0
private const val SUGGESTION_TYPE_TEXT = 1
private const val SUGGESTION_TYPE_ITEM = 2

internal class SearchSuggestionAdapter(
  private val onRecipeClick: (sharedElements: Map<View, String>, recipeId: Long) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val asyncDiff = AsyncListDiffer(this, SuggestionDiff())

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): RecyclerView.ViewHolder = when (viewType) {
    SUGGESTION_TYPE_HEADING -> {
      val binding = ComponentSearchSuggestionHeadingBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false,
      )
      ComponentSearchSuggestionHeading(binding)
    }

    SUGGESTION_TYPE_TEXT -> {
      val binding = ComponentSearchSuggestionTextBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false,
      )
      ComponentSearchSuggestionText(binding)
    }

    SUGGESTION_TYPE_ITEM -> {
      val binding =
        ComponentRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      ComponentRecipe(binding, onRecipeClick)
    }

    else -> throw IllegalStateException("Invalid view type $viewType rendering")
  }

  override fun getItemCount(): Int = asyncDiff.currentList.size

  override fun getItemViewType(position: Int): Int = when (asyncDiff.currentList[position]) {
    is Suggestion.Heading -> SUGGESTION_TYPE_HEADING
    is Suggestion.Text -> SUGGESTION_TYPE_TEXT
    is Suggestion.Item -> SUGGESTION_TYPE_ITEM
  }

  override fun onBindViewHolder(
    holder: RecyclerView.ViewHolder,
    position: Int,
  ) {
    val section = asyncDiff.currentList[position]
    when (holder.itemViewType) {
      SUGGESTION_TYPE_HEADING -> {
        val adapterItem = section as Suggestion.Heading
        (holder as ComponentSearchSuggestionHeading).bind(adapterItem)
      }

      SUGGESTION_TYPE_TEXT -> {
        val adapterItem = section as Suggestion.Text
        (holder as ComponentSearchSuggestionText).bind(adapterItem)
      }

      SUGGESTION_TYPE_ITEM -> {
        val adapterItem = section as Suggestion.Item
        (holder as ComponentRecipe).bind(adapterItem.suggestions)
      }
    }
  }

  fun reload(items: List<Suggestion>) {
    asyncDiff.submitList(items)
  }
}
