package com.mak.feastit.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal object InstantConverter {

    @TypeConverter
    @JvmStatic
    fun date(value: LocalDateTime): Long = value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    @TypeConverter
    @JvmStatic
    fun date(value: Long): LocalDateTime = Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.currentSystemDefault())

}