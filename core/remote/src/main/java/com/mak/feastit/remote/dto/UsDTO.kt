package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsDTO(
    val amount: Double?,
    val unitLong: String?,
    val unitShort: String?
)