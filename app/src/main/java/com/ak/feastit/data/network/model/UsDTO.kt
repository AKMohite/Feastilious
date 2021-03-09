package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsDTO(
    val amount: Double?,
    val unitLong: String?,
    val unitShort: String?
)