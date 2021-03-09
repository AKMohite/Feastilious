package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LengthDTO(
    val number: Int?,
    val unit: String?
)