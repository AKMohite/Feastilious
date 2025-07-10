package com.ak.feastit.ui.favorites.components

import androidx.recyclerview.widget.RecyclerView
import com.ak.feastit.R
import com.ak.feastit.databinding.ComponentSearchSuggestionTextBinding
import com.ak.feastit.ui.favorites.Suggestion
import com.ak.feastit.ui.favorites.SuggestionType

internal class ComponentSearchSuggestionText(
    private val binding: ComponentSearchSuggestionTextBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(adapterItem: Suggestion.Text) {
        binding.root.text = adapterItem.value
        val icon = if (adapterItem.type == SuggestionType.RECENT_SEARCH) {
            R.drawable.ic_recent
        } else {
            R.drawable.img_search
        }
        binding.root.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        binding.root.compoundDrawables[0].setTint(com.google.android.material.R.attr.colorSecondary)
    }

}
