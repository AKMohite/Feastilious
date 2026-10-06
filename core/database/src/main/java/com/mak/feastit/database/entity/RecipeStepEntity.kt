// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "recipe_instructions",
  indices = [Index(value = ["step_id"], unique = true)],
  foreignKeys = [
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["recipe_id"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class RecipeStepEntity(
  // recipeId+stepNo
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo(name = "step_id")
  val stepId: String,
  @ColumnInfo(name = "recipe_id")
  val recipeId: Long,
  @ColumnInfo(name = "step_no")
  val stepNo: Int,
  @ColumnInfo(name = "step_desc")
  val stepDescription: String,
  @ColumnInfo(name = "step_name")
  val stepName: String,
)
