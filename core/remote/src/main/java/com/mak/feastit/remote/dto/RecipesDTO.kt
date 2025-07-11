// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipesDTO(
  val recipes: List<RecipeDTO>?,
  val offset: Int? = 0,
  val number: Int? = 0,
  val totalResults: Int? = 0,
)
