// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity.custom

data class CartEntity(
  val id: String,
  val isBought: Boolean,
  val aisleCategory: String,
  val ingredientName: String,
  val recipeId: Long,
  val quantity: Double,
  val unit: String,
  val recipeName: String,
  val recipeImg: String,
  val servings: Int,
)
