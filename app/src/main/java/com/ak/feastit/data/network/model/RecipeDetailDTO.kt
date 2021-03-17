package com.ak.feastit.data.network.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDetailDTO(
        val name: String?,
        val steps: List<StepDTO?>?
)