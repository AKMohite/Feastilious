package com.ak.feastit.ui.favorites.components

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.favorites.Suggestion

internal class SuggestionDiff: DiffUtil.ItemCallback<Suggestion>() {

    override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}
