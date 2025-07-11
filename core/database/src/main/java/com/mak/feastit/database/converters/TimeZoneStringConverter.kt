// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object TimeZoneStringConverter {
  @TypeConverter
  @JvmStatic
  fun dateToString(value: Instant?): String? {
    if (value == null) return null
    val timeZone = TimeZone.currentSystemDefault()
    val offset = timeZone.offsetAt(value)
    val localDateTime = value.toLocalDateTime(timeZone).toString().replace("Z", "")
    return "$localDateTime$offset" // 2025-01-10T22:34:01.753+05:30
  }

  @TypeConverter
  @JvmStatic
  fun stringToDate(value: String?): Instant? = value?.let {
    val dateTime = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.parse(it)
//        dateTime.timeZoneId
    dateTime.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
  }

/*
    @TypeConverter
    @JvmStatic
    fun dateTimeToString(value: DateTimeFormat<DateTimeComponents>?): String? {

    }

    @TypeConverter
    @JvmStatic
    fun stringToDateTime(value: String?): DateTimeFormat<DateTimeComponents>? = value?.let {
        val dateTime = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.parse(it)
//        dateTime.timeZoneId
        dateTime.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
    }*/
}
