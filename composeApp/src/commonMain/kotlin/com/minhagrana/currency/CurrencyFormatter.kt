package com.minhagrana.currency

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

fun format(
    value: Double,
    currency: Currency,
): String {
    val multiplier = 10.0.pow(currency.fractionDigits)
    val rounded = round(value * multiplier) / multiplier
    val isNegative = rounded < 0
    val absValue = abs(rounded)

    val intPart = absValue.toLong()
    val intStr = formatIntPart(intPart, currency.thousandSeparator)

    val numericPart =
        if (currency.fractionDigits == 0) {
            intStr
        } else {
            val decRaw = ((absValue - intPart) * multiplier + 0.5).toLong()
            val decStr = decRaw.toString().padStart(currency.fractionDigits, '0')
            "$intStr${currency.decimalSeparator}$decStr"
        }

    val sign = if (isNegative) "-" else ""
    val space = if (currency.spaceBetweenSymbol) " " else ""
    return when (currency.symbolPosition) {
        SymbolPosition.PREFIX -> "$sign${currency.symbol}$space$numericPart"
        SymbolPosition.SUFFIX -> "$sign$numericPart$space${currency.symbol}"
    }
}

fun formatFromMinorUnits(
    minorUnits: Long,
    currency: Currency,
): String {
    val divisor = 10.0.pow(currency.fractionDigits.coerceAtLeast(0))
    val asDouble = if (currency.fractionDigits == 0) minorUnits.toDouble() else minorUnits / divisor
    return format(asDouble, currency)
}

fun parseDigitsToDouble(
    text: String,
    currency: Currency,
): Double {
    val digits = text.filter { it.isDigit() }
    if (digits.isEmpty()) return 0.0
    val parsed = digits.toLongOrNull() ?: 0L
    return if (currency.fractionDigits == 0) {
        parsed.toDouble()
    } else {
        parsed / 10.0.pow(currency.fractionDigits)
    }
}

private fun formatIntPart(
    intPart: Long,
    thousandSeparator: Char,
): String {
    if (intPart == 0L) return "0"
    return intPart
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(thousandSeparator.toString())
        .reversed()
}
