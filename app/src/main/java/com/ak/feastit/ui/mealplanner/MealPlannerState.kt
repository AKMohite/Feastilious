package com.ak.feastit.ui.mealplanner

import com.mak.feastit.domain.model.DayMealPlan
import com.mak.feastit.domain.model.MealPlanRecipe

internal data class MealPlannerState(
    val weekRange: String = "",
    val todayRecipes: List<MealPlanRecipe> = emptyList(),
    val weeklySections: List<WeekMealPlanSection> = emptyList(),
    val unscheduledRecipes: List<MealPlanRecipe> = emptyList(),
    val menuItems: List<MealPlanRecipeSheetItem> = emptyList()
)

internal sealed interface MealPlanAction {
    data class OpenMealPlanBottomSheet(val recipeId: Long): MealPlanAction
    data class OnMenuClick(val item: MealPlanSheetMenuAction, val mealPlan: MealPlanRecipe): MealPlanAction
    data class OnMealRepeat(val mealId: Long): MealPlanAction
}

internal sealed interface WeekMealPlanSection {
    fun areItemsTheSame(other: WeekMealPlanSection): Boolean
    fun areContentsTheSame(other: WeekMealPlanSection): Boolean

    data class DayHeader(val day: DayMealPlan): WeekMealPlanSection {
        override fun areItemsTheSame(other: WeekMealPlanSection): Boolean {
            return this.day == (other as? DayHeader)?.day
        }

        override fun areContentsTheSame(other: WeekMealPlanSection): Boolean {
            val dayHeader = other as? DayHeader ?: return false
            return this.day.isSameAs(dayHeader.day)
        }
    }

    data class MealRecipe(val meal: MealPlanRecipe, val isExpanded: Boolean = true): WeekMealPlanSection {
        override fun areItemsTheSame(other: WeekMealPlanSection): Boolean {
            return this == other
        }

        override fun areContentsTheSame(other: WeekMealPlanSection): Boolean {
            val mealPlan = other as? MealRecipe ?: return false
            return this.meal.isSameAs(mealPlan.meal) && mealPlan.isExpanded == isExpanded
        }
    }
}

