package com.ak.feastit.data.network.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDetailDTO(
        val name: String?,
        val steps: List<StepDTO?>?
)