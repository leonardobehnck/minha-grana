package com.minhagrana.currency

enum class SymbolPosition { PREFIX, SUFFIX }

data class Currency(
    val code: String,
    val symbol: String,
    val decimalSeparator: Char,
    val thousandSeparator: Char,
    val symbolPosition: SymbolPosition,
    val spaceBetweenSymbol: Boolean,
    val fractionDigits: Int = 2,
)
