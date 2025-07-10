package com.ak.feastit.ui.favorites.components

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Recipe

internal class RecipeDiff: DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.isSameAs(newItem)
    }

}
