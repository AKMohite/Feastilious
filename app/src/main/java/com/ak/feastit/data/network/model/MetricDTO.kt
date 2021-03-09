package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetricDTO(
    val amount: Double?,
    val unitLong: String?,
    val unitShort: String?
)