// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExtendedIngredientDTO(
//    val aisle: String?,
  val amount: Double? = null,
  val consistency: String? = null,
  val id: Int? = null,
  val image: String? = null,
//    val measures: MeasuresDTO?,
//    val meta: List<String>?,
//    val metaInformation: List<String>?,
  val name: String? = null,
//    val nameClean: String?,
  val original: String? = null,
//    val originalName: String?,
//    val originalString: String?,
  val unit: String? = null,
)
