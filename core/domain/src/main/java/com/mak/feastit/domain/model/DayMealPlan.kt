// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class DayMealPlan(
  val day: String,
  val noOfMeal: String,
  val isExpanded: Boolean = true,
) {
  fun isSameAs(other: DayMealPlan): Boolean = day == other.day &&
    noOfMeal == other.noOfMeal
}
