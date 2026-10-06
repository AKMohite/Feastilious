// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Collections.emptyList

@JsonClass(generateAdapter = true)
data class ComplexSearchDTO(
  @Json(name = "number")
  val number: Int? = 0,
  @Json(name = "offset")
  val offset: Int? = 0,
  @Json(name = "results")
  val results: List<RecipeDTO>? = emptyList(),
  @Json(name = "totalResults")
  val totalResults: Int? = 0,
)
