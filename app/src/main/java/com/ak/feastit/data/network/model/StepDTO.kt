package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StepDTO(
    val equipment: List<EquipmentDTO>?,
    val ingredients: List<IngredientDTO>?,
    val length: LengthDTO?,
    val number: Int? = 0,
    val step: String? = ""
)