package com.mak.feastit.remote.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDetailDTO(
        val name: String?,
        val steps: List<StepDTO?>?
)