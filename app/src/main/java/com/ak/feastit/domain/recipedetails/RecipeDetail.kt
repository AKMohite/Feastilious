package com.ak.feastit.domain.recipedetails

data class RecipeDetail(
        val id: Long,
        val recipeName: String,
        val recipeImgUrl: String,
        val subRecipes: List<SubRecipe>
)