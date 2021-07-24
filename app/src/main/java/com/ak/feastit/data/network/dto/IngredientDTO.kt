package com.ak.feastit.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientDTO(
    val id: Long?,
    val image: String?,
    val localizedName: String?,
    val name: String?
)