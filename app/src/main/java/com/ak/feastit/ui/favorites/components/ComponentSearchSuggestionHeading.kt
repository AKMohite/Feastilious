package com.ak.feastit.ui.favorites.components

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.databinding.ComponentSearchSuggestionHeadingBinding
import com.ak.feastit.ui.favorites.Suggestion

internal class ComponentSearchSuggestionHeading(
    private val binding: ComponentSearchSuggestionHeadingBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(adapterItem: Suggestion.Heading) {
        binding.root.text = binding.root.context.getString(adapterItem.title)
    }

}
