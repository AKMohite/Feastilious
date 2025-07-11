// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// TODO add indices to child column foreign key
@Entity(
  tableName = "popular_recipes",
  foreignKeys = [
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["recipe_id"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class PopularRecipeEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "recipe_id")
  val recipeId: Long,
  val page: Int,
)
