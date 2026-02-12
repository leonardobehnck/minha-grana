package com.minhagrana.ui

import com.minhagrana.entities.Category
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime


fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return email.matches(emailRegex)
}

fun formatDoubleToBRL(value: Double): String {
    val rounded = kotlin.math.round(value * 100) / 100
    val isNegative = rounded < 0
    val absValue = kotlin.math.abs(rounded)

    val intPart = absValue.toLong()
    val decPart = ((absValue - intPart) * 100 + 0.5).toLong()

    val intStr = if (intPart == 0L) "0" else intPart.toString().reversed().chunked(3).joinToString(".").reversed()
    val decStr = decPart.toString().padStart(2, '0')

    val sign = if (isNegative) "-" else ""
    return "${sign}R$ $intStr,$decStr"
}

private val monthNamesPtBr = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
)

val currentMonth: String
    get() {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val monthIndex = localDateTime.month.number - 1
        return monthNamesPtBr.getOrElse(monthIndex) { "Null" }
    }

fun getCurrentDate(): String {
    val now = Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.month.number.toString().padStart(2, '0')
    val year = localDateTime.year
    return "$day/$month/$year"
}

fun processMonthDataByExpense(month: Month): Map<Category, Double> {
    val expenseEntries = month.entries.filter { it.type == EntryType.EXPENSE }
    return expenseEntries
        .groupBy { it.category }
        .mapValues { (_, entries) -> entries.sumOf { it.value } }
}
