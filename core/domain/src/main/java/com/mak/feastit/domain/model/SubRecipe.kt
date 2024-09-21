package com.mak.feastit.domain.model

data class SubRecipe(
    val recipeName: String,
    val ingredients: List<Ingredient>,
    val instructions: List<RecipeInstruction>
)