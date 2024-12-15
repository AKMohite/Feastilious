package com.mak.feastit.database.converters

import androidx.room.TypeConverter
import java.time.Instant

internal object InstantConverter {

    @TypeConverter
    @JvmStatic
    fun date(value: Instant): Long = value.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun date(value: Long): Instant = Instant.ofEpochMilli(value)

}