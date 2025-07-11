// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "recipe_nutrients",
  foreignKeys = [
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["recipe_id"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class NutrientEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,
  @ColumnInfo("recipe_id")
  val recipeId: Long,
  val name: String,
  val amount: Double,
  val unit: String,
  @ColumnInfo("percent_of_daily_need")
  val percentOfDailyNeed: Double,
)
