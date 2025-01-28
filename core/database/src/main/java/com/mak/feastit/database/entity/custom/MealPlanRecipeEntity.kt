package com.mak.feastit.database.entity.custom

import androidx.room.ColumnInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class MealPlanRecipeEntity(
    val id: Long,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long,
    @ColumnInfo(name = "planned_for")
    val scheduledFor: Instant?,
    @ColumnInfo(name = "notification_time")
    val notificationTime: Instant?,
    @ColumnInfo(name = "is_made")
    val isMade: Boolean,
    val name: String,
    @ColumnInfo(name = "img")
    val image: String,
    @ColumnInfo("preparation_time")
    val preparationTime: Int
)