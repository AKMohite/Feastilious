package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.MealPlanRecipe

internal class UnscheduledRecipeDiff: DiffUtil.ItemCallback<MealPlanRecipe>() {
    override fun areItemsTheSame(oldItem: MealPlanRecipe, newItem: MealPlanRecipe): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MealPlanRecipe, newItem: MealPlanRecipe): Boolean {
        return oldItem.isSameAs(newItem)
    }

}
