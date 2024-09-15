package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExtendedIngredientDTO(
    val aisle: String?,
    val amount: Double?,
    val consistency: String?,
    val id: Int?,
    val image: String?,
    val measures: MeasuresDTO?,
    val meta: List<String>?,
    val metaInformation: List<String>?,
    val name: String?,
    val nameClean: String?,
    val original: String?,
    val originalName: String?,
    val originalString: String?,
    val unit: String?
)