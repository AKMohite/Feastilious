package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientDTO(
    val id: Int?,
    val image: String?,
    val localizedName: String?,
    val name: String?
)