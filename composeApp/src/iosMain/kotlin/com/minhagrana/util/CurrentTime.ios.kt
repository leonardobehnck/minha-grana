package com.minhagrana.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.posix.time

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
private fun currentInstant() = Instant.fromEpochSeconds(time(null).toLong())

actual fun currentMonthNumber(): Int =
    currentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .monthNumber

actual fun currentYear(): Int =
    currentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year

actual fun getCurrentDateString(): String {
    val now = currentInstant().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.dayOfMonth.toString().padStart(2, '0')
    val month = now.monthNumber.toString().padStart(2, '0')
    return "$day/$month/${now.year}"
}
