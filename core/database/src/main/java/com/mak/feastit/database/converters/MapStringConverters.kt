// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.converters

import androidx.room.TypeConverter

private const val KEY_VALUE_SEPARATOR = "->"
private const val ENTRY_SEPARATOR = "||"

/**
 * Reference: [Stack overflow](https://stackoverflow.com/a/68783608)
 */
internal object MapStringConverters {
  /**
   * return key1->value1||key2->value2||key3->value3
   */
  @TypeConverter
  fun mapToString(map: Map<String, String>): String = map.entries.joinToString(separator = ENTRY_SEPARATOR) {
    "${it.key}$KEY_VALUE_SEPARATOR${it.value}"
  }

  /**
   * return map of String, String
   *        "key1": "value1"
   *        "key2": "value2"
   *        "key3": "value3"
   */
  @TypeConverter
  fun stringToMap(string: String): Map<String, String> {
    if (string.isBlank() || !string.contains(KEY_VALUE_SEPARATOR)) return emptyMap()
    return string
      .split(ENTRY_SEPARATOR)
      .map {
        val (key, value) = it.split(KEY_VALUE_SEPARATOR)
        key to value
      }.toMap()
  }
}
