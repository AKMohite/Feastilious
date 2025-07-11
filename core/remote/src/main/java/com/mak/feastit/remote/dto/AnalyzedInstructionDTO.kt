// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote.dto

import com.squareup.moshi.JsonClass
import java.util.Collections.emptyList

@JsonClass(generateAdapter = true)
data class AnalyzedInstructionDTO(
  val name: String? = "",
  val steps: List<StepDTO>? = emptyList(),
)
