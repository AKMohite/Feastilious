// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EquipmentDTO(
  val id: Int? = null,
//    val image: String?,
//    val localizedName: String?,
  val name: String? = "",
)
