// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "similar_recipes",
  foreignKeys = [
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["recipe_id"],
      onDelete = ForeignKey.CASCADE,
    ),
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["parent_id"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class SimilarRecipeEntity(
  @PrimaryKey(autoGenerate = false)
  val id: String,
  @ColumnInfo(name = "recipe_id")
  val recipeId: Long,
  @ColumnInfo(name = "parent_id")
  val parentRecipeId: Long,
)
