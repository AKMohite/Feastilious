package com.mak.feastit.domain.model

import kotlinx.datetime.LocalDateTime


data class MealPlanRecipe(
    val recipeId: Long,
    val scheduledFor: LocalDateTime?,
    val isMade: Boolean,
    val name: String,
    val image: String
) {
    fun isSameAs(other: MealPlanRecipe): Boolean {
        return recipeId == other.recipeId &&
                scheduledFor == other.scheduledFor &&
                isMade == other.isMade
    }
}
