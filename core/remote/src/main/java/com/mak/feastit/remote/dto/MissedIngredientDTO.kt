// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MissedIngredientDTO(
  val aisle: String?,
  val amount: Double?,
  val extendedName: String?,
  val id: Int?,
  val image: String?,
  val meta: List<String?>?,
  val metaInformation: List<String?>?,
  val name: String?,
  val original: String?,
  val originalName: String?,
  val originalString: String?,
  val unit: String?,
  val unitLong: String?,
  val unitShort: String?,
)
