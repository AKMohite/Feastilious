// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.tabs.week

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.WeekMealPlanSection

internal class MealPlanWeekDiff : DiffUtil.ItemCallback<WeekMealPlanSection>() {
  override fun areItemsTheSame(
    oldItem: WeekMealPlanSection,
    newItem: WeekMealPlanSection,
  ): Boolean = oldItem.areItemsTheSame(newItem)

  override fun areContentsTheSame(
    oldItem: WeekMealPlanSection,
    newItem: WeekMealPlanSection,
  ): Boolean = oldItem.areContentsTheSame(newItem)
}
