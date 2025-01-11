package com.mak.feastit.domain.model

import kotlinx.datetime.LocalDateTime


data class MealPlan(
    val recipeId: Long,
    val scheduledFor: LocalDateTime?,
    val isMade: Boolean
)
