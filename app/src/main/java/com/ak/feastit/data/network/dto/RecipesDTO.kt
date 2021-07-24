package com.ak.feastit.data.network.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipesDTO(
    val recipes: List<RecipeDTO>?,
    val offset: Int? = 0,
    val number: Int? = 0,
    val totalResults: Int? = 0
)