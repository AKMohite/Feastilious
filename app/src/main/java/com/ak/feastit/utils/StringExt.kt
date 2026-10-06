// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.utils

import java.util.Locale

internal fun String.getEnumTitle(): String = this
  .replace("_", " ")
  .replace(" chips", "", ignoreCase = true)
  .lowercase(Locale.getDefault())
  .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
