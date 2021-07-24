package com.ak.feastit.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasuresDTO(
    val metric: MetricDTO?,
    val us: UsDTO?
)