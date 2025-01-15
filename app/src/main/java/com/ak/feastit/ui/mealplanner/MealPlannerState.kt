package com.ak.feastit.ui.mealplanner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ak.feastit.R
import com.mak.feastit.domain.model.MealPlanRecipe

data class MealPlannerState(
    val weekRange: String = "",
    val todayRecipes: List<MealPlanRecipe> = emptyList(),
    val weeklyRecipes: Map<String, List<MealPlanRecipe>> = emptyMap(),
    val unscheduledRecipes: List<MealPlanRecipe> = emptyList()
)

internal sealed interface MealPlanAction {
    data class OpenMealPlanBottomSheet(val recipeId: Long): MealPlanAction
}
