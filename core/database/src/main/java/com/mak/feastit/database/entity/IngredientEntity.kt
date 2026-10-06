// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "recipe_ingredients",
  foreignKeys = [
    ForeignKey(
      entity = RecipeEntity::class,
      parentColumns = ["id"],
      childColumns = ["recipe_id"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class IngredientEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo(name = "id")
  val id: String,
  @ColumnInfo(name = "ingredient_id")
  val ingredientId: Long,
  @ColumnInfo(name = "recipe_id")
  val recipeId: Long,
  @ColumnInfo(name = "aisle_category")
  val aisle: String? = null,
  @ColumnInfo(name = "ingredient_name")
  val ingredientName: String,
//    @ColumnInfo(name = DB_INGREDIENT_CONSISTENCY)
//    val ingredientConsistency: String,
  @ColumnInfo(name = "img")
  val ingredientImg: String,
  @ColumnInfo(name = "quantity")
  val quantity: Double,
  @ColumnInfo(name = "unit")
  val unit: String,
)
