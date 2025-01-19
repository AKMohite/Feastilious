package com.mak.feastit.domain.model

data class DayMealPlan(
    val day: String,
    val noOfMeal: String,
    val isExpanded: Boolean = false
) {
    fun isSameAs(other: DayMealPlan): Boolean {
        return day == other.day &&
                noOfMeal == other.noOfMeal
    }
}