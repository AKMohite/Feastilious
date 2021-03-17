package com.ak.feastit.domain.recipedetails

data class Ingredient(
        val id: Long,
        val image: String,
        val localizedName: String,
        val name: String
)