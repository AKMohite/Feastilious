package com.ak.feastit.domain.model


data class RecipeDetail(
        val recipeId: Long,
        val recipeName: String,
        val recipeSummary: String,
        val recipeImg: String,
        val recipeSource: String,
        val recipeReadyInMins: Int,
        val servings: Int,
        val pricePerServing: Double,
        val sourceName: String,
        val isAdded: Boolean = false,
        val instructions: List<Instruction> = emptyList(),
        val ingredients: List<Ingredient> = emptyList()
)