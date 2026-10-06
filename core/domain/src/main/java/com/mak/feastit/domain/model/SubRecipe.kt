// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class SubRecipe(
  val recipeName: String,
  val ingredients: List<Ingredient>,
  val instructions: List<RecipeInstruction>,
)
