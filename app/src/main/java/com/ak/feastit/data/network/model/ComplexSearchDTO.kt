package com.ak.feastit.data.network.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComplexSearchDTO(
    val number: Int,
    val offset: Int,
    val results: List<RecipeDTO>?,
    val totalResults: Int
)