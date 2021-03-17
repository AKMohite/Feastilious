package com.ak.feastit.domain.recipedetails

data class SubRecipe(
        val recipeName: String,
        val ingredients: List<Ingredient>,
        val instructions: List<RecipeInstruction>
)