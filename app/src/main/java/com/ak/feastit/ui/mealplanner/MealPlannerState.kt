package com.ak.feastit.ui.mealplanner

import com.mak.feastit.domain.model.MealPlanRecipe

data class MealPlannerState(
    val todayRecipes: List<MealPlanRecipe> = emptyList(),
    val weeklyRecipes: List<MealPlanRecipe> = emptyList(),
    val unscheduledRecipes: List<MealPlanRecipe> = emptyList()
)
