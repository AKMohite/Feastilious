package com.ak.feastit.ui.mealplanner.components

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.mealplanner.MealPlanRecipeSheetItem

internal class MealPlanMenuDiff: DiffUtil.ItemCallback<MealPlanRecipeSheetItem>() {
    override fun areItemsTheSame(
        oldItem: MealPlanRecipeSheetItem,
        newItem: MealPlanRecipeSheetItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MealPlanRecipeSheetItem,
        newItem: MealPlanRecipeSheetItem
    ): Boolean {
        return oldItem.isSameAs(newItem)
    }

}
