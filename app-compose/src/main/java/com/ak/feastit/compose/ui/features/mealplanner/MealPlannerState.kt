// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.mealplanner

import com.mak.feastit.domain.model.MealPlanRecipe
import com.mak.feastit.domain.model.WeekMealPlanSection

internal data class MealPlannerState(
  val weekRange: String = "",
  val todayRecipes: List<MealPlanRecipe> = emptyList(),
  val weeklySections: List<WeekMealPlanSection> = emptyList(),
  val unscheduledRecipes: List<MealPlanRecipe> = emptyList(),
  val menuItems: List<MealPlanRecipeSheetItem> = emptyList(),
)

internal sealed interface MealPlanAction {
  data class OpenMealPlanBottomSheet(
    val recipeId: Long,
  ) : MealPlanAction

  data class OnMenuClick(
    val item: MealPlanSheetMenuAction,
    val mealPlan: MealPlanRecipe,
  ) : MealPlanAction

  data class OnMealRepeat(
    val mealId: Long,
  ) : MealPlanAction
}

internal data class MealPlanRecipeSheetItem(
  val title: Int,
  val icon: Int,
  val action: MealPlanSheetMenuAction,
)

internal enum class MealPlanSheetMenuAction {
  RESCHEDULE,
  REPEAT,
  DOWNLOAD,
  SHARE,
  REMOVE,
}
