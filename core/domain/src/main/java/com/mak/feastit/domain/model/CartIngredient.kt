// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class CartIngredient(
  val id: String,
  val isBought: Boolean,
  val aisleCategory: String,
  val ingredientName: String,
  val recipeId: Long,
  val quantity: String,
  val recipeName: String,
  val recipeImg: RecipeImage,
  val servings: Int,
) {
  fun isSameAs(ingredient: CartIngredient): Boolean = this.id == ingredient.id &&
    this.ingredientName == ingredient.ingredientName &&
    this.isBought == ingredient.isBought
}
