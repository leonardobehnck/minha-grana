package com.minhagrana.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual fun currentMonthNumber(): Int =
    Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .month.number

actual fun currentYear(): Int =
    Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year

actual fun getCurrentDateString(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.day.toString().padStart(2, '0')
    val month = now.month.number.toString().padStart(2, '0')
    return "$day/$month/${now.year}"
}
