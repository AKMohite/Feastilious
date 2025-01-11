package com.mak.feastit.domain.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun LocalDate.daysShift(days: Int): LocalDate = when {
    days < 0 -> {
        minus(1, DateTimeUnit.DayBased(-days))
    }
    days > 0 -> {
        plus(1, DateTimeUnit.DayBased(days))
    }
    else -> this
}