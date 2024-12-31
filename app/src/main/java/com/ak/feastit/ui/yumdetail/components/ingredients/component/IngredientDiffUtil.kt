package com.ak.feastit.ui.yumdetail.components.ingredients.component

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.Ingredient

internal class IngredientDiffUtil: DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.name == newItem.name
    }

}
