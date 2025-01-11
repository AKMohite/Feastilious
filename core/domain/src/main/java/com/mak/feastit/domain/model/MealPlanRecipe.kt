package com.mak.feastit.domain.model

import kotlinx.datetime.LocalDateTime


data class MealPlanRecipe(
    val recipeId: Long,
    val scheduledFor: LocalDateTime?,
    val isMade: Boolean,
    val name: String,
    val image: String
)
