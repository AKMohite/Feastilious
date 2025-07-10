package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StepDTO(
//    val equipment: List<EquipmentDTO>? = emptyList(),
    val ingredients: List<IngredientDTO>? = emptyList(),
//    val length: LengthDTO?,
    val number: Int? = 0,
    val step: String? = ""
)