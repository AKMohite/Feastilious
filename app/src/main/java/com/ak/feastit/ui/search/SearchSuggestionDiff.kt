// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search

import androidx.recyclerview.widget.DiffUtil

internal class SearchSuggestionDiff : DiffUtil.ItemCallback<SearchSuggestionItem>() {
  override fun areItemsTheSame(
    oldItem: SearchSuggestionItem,
    newItem: SearchSuggestionItem,
  ): Boolean = oldItem.areItemsSame(newItem)

  override fun areContentsTheSame(
    oldItem: SearchSuggestionItem,
    newItem: SearchSuggestionItem,
  ): Boolean = oldItem.areContentsSame(newItem)
}
