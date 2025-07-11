// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.tabs.unscheduled.components

import androidx.recyclerview.widget.DiffUtil
import com.mak.feastit.domain.model.MealPlanRecipe

internal class UnscheduledRecipeDiff : DiffUtil.ItemCallback<MealPlanRecipe>() {
  override fun areItemsTheSame(
    oldItem: MealPlanRecipe,
    newItem: MealPlanRecipe,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: MealPlanRecipe,
    newItem: MealPlanRecipe,
  ): Boolean = oldItem.isSameAs(newItem)
}
