package com.mak.feastit.domain.model

data class CartIngredient(
    val id: String,
    val isBought: Boolean,
    val aisleCategory: String,
    val ingredientName: String,
    val recipeId: Long,
    val quantity: String,
    val recipeName: String,
    val recipeImg: String,
    val servings: Int
)