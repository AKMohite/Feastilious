package com.ak.feastit.ui.yumdetail.components.ingredients.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.IngredientSection
import com.mak.feastit.domain.model.Ingredient

internal class IngredientDiffUtil: DiffUtil.ItemCallback<IngredientSection>() {
    override fun areItemsTheSame(oldItem: IngredientSection, newItem: IngredientSection): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: IngredientSection, newItem: IngredientSection): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}
