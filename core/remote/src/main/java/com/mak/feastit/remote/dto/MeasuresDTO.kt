package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeasuresDTO(
    val metric: MetricDTO?,
    val us: UsDTO?
)