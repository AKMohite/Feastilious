package com.ak.feastit.ui.yumdetail.tabs.ingredients.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.IngredientSection

internal class IngredientDiffUtil: DiffUtil.ItemCallback<IngredientSection>() {
    override fun areItemsTheSame(oldItem: IngredientSection, newItem: IngredientSection): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: IngredientSection, newItem: IngredientSection): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}
