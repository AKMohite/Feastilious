package com.mak.feastit.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun LocalDate.daysShift(days: Int): LocalDate = when {
    days < 0 -> {
        minus(1, DateTimeUnit.DayBased(-days))
    }
    days > 0 -> {
        plus(1, DateTimeUnit.DayBased(days))
    }
    else -> this
}

fun defaultNow(): Instant = Clock.System.now()

fun Instant.defaultLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    return toLocalDateTime(timeZone)
}

fun Instant.defaultLocalDate(): LocalDate {
    return toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun Instant.dayMonth(): String {
    val format = LocalDateTime.Format { byUnicodePattern("dd MM") }
    val dateTime = defaultLocalDateTime()
    return dateTime.format(format)
}

fun Instant.defaultLocalTime(): LocalTime {
    return toLocalDateTime(TimeZone.currentSystemDefault()).time
}