package com.ak.feastit.ui.mealplanner.tabs.week

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.mealplanner.WeekMealPlanSection

internal class MealPlanWeekDiff: DiffUtil.ItemCallback<WeekMealPlanSection>() {
    override fun areItemsTheSame(
        oldItem: WeekMealPlanSection,
        newItem: WeekMealPlanSection
    ): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: WeekMealPlanSection,
        newItem: WeekMealPlanSection
    ): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

}
