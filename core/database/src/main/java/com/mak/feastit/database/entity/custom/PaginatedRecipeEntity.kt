// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity.custom

import androidx.room.ColumnInfo

data class PaginatedRecipeEntity(
  val id: Long,
  @ColumnInfo(name = "name")
  val recipeName: String,
  @ColumnInfo(name = "summary")
  val recipeSummary: String,
  @ColumnInfo(name = "image_extension")
  val extension: String,
  val page: Int,
)
