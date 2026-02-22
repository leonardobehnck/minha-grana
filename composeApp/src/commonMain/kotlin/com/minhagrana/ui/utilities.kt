package com.minhagrana.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.minhagrana.entities.Category
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.util.currentMonthNumber
import com.minhagrana.util.getCurrentDateString

fun parseBRLInputToDouble(text: String): Double {
    val digits = text.filter { it.isDigit() }
    if (digits.isEmpty()) return 0.0
    return (digits.toLongOrNull() ?: 0L) / 100.0
}

fun formatDoubleToBRL(value: Double): String {
    val rounded = kotlin.math.round(value * 100) / 100
    val isNegative = rounded < 0
    val absValue = kotlin.math.abs(rounded)

    val intPart = absValue.toLong()
    val decPart = ((absValue - intPart) * 100 + 0.5).toLong()

    val intStr =
        if (intPart == 0L) {
            "0"
        } else {
            intPart
                .toString()
                .reversed()
                .chunked(3)
                .joinToString(".")
                .reversed()
        }
    val decStr = decPart.toString().padStart(2, '0')

    val sign = if (isNegative) "-" else ""
    return "${sign}R$ $intStr,$decStr"
}

@Composable
fun balanceColor(value: Double): Color =
    when {
        value < 0 -> MaterialTheme.colorScheme.error
        value == 0.0 -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.primary
    }

private val monthNamesPtBr =
    listOf(
        "Janeiro",
        "Fevereiro",
        "Março",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro",
    )

val currentMonth: String
    get() = monthNamesPtBr.getOrElse(currentMonthNumber() - 1) { "Null" }

fun getCurrentDate(): String = getCurrentDateString()

fun parseDateDDMMYYYY(date: String): Triple<Int, Int, Int>? {
    val parts = date.split("/")
    if (parts.size != 3) return null
    val day = parts[0].toIntOrNull() ?: return null
    val month = parts[1].toIntOrNull() ?: return null
    val year = parts[2].toIntOrNull() ?: return null
    if (month !in 1..12) return null
    return Triple(day, month, year)
}

fun processMonthDataByExpense(month: Month): Map<Category, Double> {
    val expenseEntries = month.entries.filter { it.type == EntryType.EXPENSE }
    return expenseEntries
        .groupBy { it.category }
        .mapValues { (_, entries) -> entries.sumOf { it.value } }
}
