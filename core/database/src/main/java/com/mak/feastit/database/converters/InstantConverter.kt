// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

internal object InstantConverter {
  //    UTC date time format
  @TypeConverter
  @JvmStatic
  fun instantToString(value: Instant?): String? {
    if (value == null) return null
    return value.toString()
  }

  @TypeConverter
  @JvmStatic
  fun stringToInstant(value: String?): Instant? = value?.let {
    Instant.parse(it)
  }
}
