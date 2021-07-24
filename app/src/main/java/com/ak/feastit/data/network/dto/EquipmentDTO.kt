package com.ak.feastit.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EquipmentDTO(
    val id: Int?,
    val image: String?,
    val localizedName: String?,
    val name: String?
)