// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

sealed interface WeekMealPlanSection {
  fun areItemsTheSame(other: WeekMealPlanSection): Boolean

  fun areContentsTheSame(other: WeekMealPlanSection): Boolean

  data class DayHeader(
    val day: DayMealPlan,
  ) : WeekMealPlanSection {
    override fun areItemsTheSame(other: WeekMealPlanSection): Boolean = this.day == (other as? DayHeader)?.day

    override fun areContentsTheSame(other: WeekMealPlanSection): Boolean {
      val dayHeader = other as? DayHeader ?: return false
      return this.day.isSameAs(dayHeader.day)
    }
  }

  data class MealRecipe(
    val meal: MealPlanRecipe,
    val isExpanded: Boolean = true,
  ) : WeekMealPlanSection {
    override fun areItemsTheSame(other: WeekMealPlanSection): Boolean = this == other

    override fun areContentsTheSame(other: WeekMealPlanSection): Boolean {
      val mealPlan = other as? MealRecipe ?: return false
      return this.meal.isSameAs(mealPlan.meal) && mealPlan.isExpanded == isExpanded
    }
  }
}
