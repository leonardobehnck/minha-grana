package com.minhagrana.util

/**
 * Returns current month number (1-12). Expect/actual to avoid Clock.System in common code (iOS resolution issue).
 */
expect fun currentMonthNumber(): Int

/**
 * Returns current year (e.g. 2025). Expect/actual for iOS.
 */
expect fun currentYear(): Int

/**
 * Returns current date as "dd/MM/yyyy". Expect/actual for iOS.
 */
expect fun getCurrentDateString(): String
