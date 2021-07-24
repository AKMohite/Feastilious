package com.ak.feastit.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LengthDTO(
    val number: Int?,
    val unit: String?
)