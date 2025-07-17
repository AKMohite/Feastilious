// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.model

data class RecipeDetail(
  val recipeId: Long,
  val recipeName: String,
  val recipeSummary: String,
  val recipeImg: RecipeImage,
  val recipeSource: String,
  val recipeReadyInMins: Int,
  val servings: Int,
  val pricePerServing: Double,
  val sourceName: String,
  val isAddedToCollection: Boolean = false,
  val instructions: List<Instruction> = emptyList(),
  val ingredients: List<Ingredient> = emptyList(),
)
