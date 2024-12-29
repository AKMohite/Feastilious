package com.ak.feastit.ui.legacyrecipes

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Recipe

class LegacyRecipeComparator : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
}