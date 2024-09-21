package com.ak.feastit.ui.recipedetail

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Ingredient

class IngredientComparator : DiffUtil.ItemCallback<Ingredient>() {

    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient) =
            oldItem == newItem
}