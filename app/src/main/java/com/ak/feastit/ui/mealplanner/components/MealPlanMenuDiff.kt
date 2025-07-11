// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.mealplanner.components

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.mealplanner.MealPlanRecipeSheetItem

internal class MealPlanMenuDiff : DiffUtil.ItemCallback<MealPlanRecipeSheetItem>() {
  override fun areItemsTheSame(
    oldItem: MealPlanRecipeSheetItem,
    newItem: MealPlanRecipeSheetItem,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: MealPlanRecipeSheetItem,
    newItem: MealPlanRecipeSheetItem,
  ): Boolean = oldItem.isSameAs(newItem)
}
