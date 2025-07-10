package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LengthDTO(
    val number: Int?,
    val unit: String?
)