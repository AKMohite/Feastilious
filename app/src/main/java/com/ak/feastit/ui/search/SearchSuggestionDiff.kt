package com.ak.feastit.ui.search

import androidx.recyclerview.widget.DiffUtil

internal class SearchSuggestionDiff : DiffUtil.ItemCallback<SearchSuggestionItem>() {
    override fun areItemsTheSame(
        oldItem: SearchSuggestionItem,
        newItem: SearchSuggestionItem
    ): Boolean {
        return oldItem.areItemsSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: SearchSuggestionItem,
        newItem: SearchSuggestionItem
    ): Boolean {
        return oldItem.areContentsSame(newItem)
    }

}
