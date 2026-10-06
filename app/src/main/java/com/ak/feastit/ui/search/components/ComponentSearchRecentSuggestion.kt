// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search.components

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentSearchSuggestionTextBinding
import com.ak.feastit.ui.search.SearchSuggestionItem
import com.ak.feastit.utils.onClick

class ComponentSearchRecentSuggestion(
  private val binding: ComponentSearchSuggestionTextBinding,
  private val onHistoryClick: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(adapterItem: SearchSuggestionItem.History) {
    binding.root.text = adapterItem.result
    binding.root.onClick {
      onHistoryClick(adapterItem.result)
    }
  }
}
