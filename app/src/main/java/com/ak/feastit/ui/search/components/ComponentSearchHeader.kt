package com.ak.feastit.ui.search.components

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentSearchHeaderItemBinding
import com.ak.feastit.ui.search.SearchSuggestionItem

class ComponentSearchHeader(
    private val binding: ComponentSearchHeaderItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adapterItem: SearchSuggestionItem.Header) {
        binding.root.text = adapterItem.name
    }

}
