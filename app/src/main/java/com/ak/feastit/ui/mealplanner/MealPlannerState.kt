package com.ak.feastit.ui.mealplanner

import com.mak.feastit.domain.model.MealPlanRecipe

data class MealPlannerState(
    val weekRange: String = "",
    val todayRecipes: List<MealPlanRecipe> = emptyList(),
    val weeklyRecipes: Map<String, List<MealPlanRecipe>> = emptyMap(),
    val unscheduledRecipes: List<MealPlanRecipe> = emptyList()
)
