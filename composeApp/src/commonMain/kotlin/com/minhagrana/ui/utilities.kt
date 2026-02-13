package com.minhagrana.ui

import com.minhagrana.entities.Category
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.util.currentMonthNumber
import com.minhagrana.util.getCurrentDateString

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return email.matches(emailRegex)
}

/**
 * Parses the raw input from a BRL-formatted field (BRLVisualTransformation).
 * The field stores digits as centavos (e.g. "500" = R$ 5,00). Returns value in reais.
 */
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

fun processMonthDataByExpense(month: Month): Map<Category, Double> {
    val expenseEntries = month.entries.filter { it.type == EntryType.EXPENSE }
    return expenseEntries
        .groupBy { it.category }
        .mapValues { (_, entries) -> entries.sumOf { it.value } }
}
