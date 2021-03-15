package com.ak.feastit.ui.recipes

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.domain.recipelist.Recipe

class RecipeComparator : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
}