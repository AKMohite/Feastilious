package com.mak.feastit.remote.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComplexSearchDTO(
    val number: Int,
    val offset: Int,
    val results: List<RecipeDTO>?,
    val totalResults: Int
)