// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity.custom

import androidx.room.ColumnInfo
import kotlinx.datetime.Instant

data class MealPlanRecipeEntity(
  val id: Long,
  @ColumnInfo(name = "recipe_id")
  val recipeId: Long,
  @ColumnInfo(name = "planned_for")
  val scheduledFor: Instant?,
//    @ColumnInfo(name = "notification_time")
//    val notificationTime: Instant?,
  @ColumnInfo(name = "is_made")
  val isMade: Boolean,
  val name: String,
//  @ColumnInfo(name = "image_extension")
//  val extension,
  @ColumnInfo("preparation_time")
  val preparationTime: Int,
)
