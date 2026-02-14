package com.minhagrana.util

import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import platform.posix.time

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
private fun currentInstant(): Instant = Instant.fromEpochSeconds(time(null))

actual fun currentMonthNumber(): Int =
    currentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .month.number

actual fun currentYear(): Int =
    currentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year

actual fun getCurrentDateString(): String {
    val now = currentInstant().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.day.toString().padStart(2, '0')
    val month = now.month.number.toString().padStart(2, '0')
    return "$day/$month/${now.year}"
}
