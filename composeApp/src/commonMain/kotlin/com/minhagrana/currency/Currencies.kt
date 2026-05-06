package com.minhagrana.currency

object Currencies {
    val BRL = Currency("BRL", "R$", ',', '.', SymbolPosition.PREFIX, true)
    val USD = Currency("USD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val EUR = Currency("EUR", "€", ',', '.', SymbolPosition.SUFFIX, true)
    val GBP = Currency("GBP", "£", '.', ',', SymbolPosition.PREFIX, false)
    val JPY = Currency("JPY", "¥", '.', ',', SymbolPosition.PREFIX, false, fractionDigits = 0)
    val CNY = Currency("CNY", "¥", '.', ',', SymbolPosition.PREFIX, false)
    val ARS = Currency("ARS", "$", ',', '.', SymbolPosition.PREFIX, true)
    val MXN = Currency("MXN", "$", '.', ',', SymbolPosition.PREFIX, false)
    val CLP = Currency("CLP", "$", ',', '.', SymbolPosition.PREFIX, true, fractionDigits = 0)
    val COP = Currency("COP", "$", ',', '.', SymbolPosition.PREFIX, true)
    val PEN = Currency("PEN", "S/", '.', ',', SymbolPosition.PREFIX, true)
    val UYU = Currency("UYU", "\$U", ',', '.', SymbolPosition.PREFIX, true)
    val CAD = Currency("CAD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val AUD = Currency("AUD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val NZD = Currency("NZD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val CHF = Currency("CHF", "CHF", '.', '\'', SymbolPosition.PREFIX, true)
    val SEK = Currency("SEK", "kr", ',', ' ', SymbolPosition.SUFFIX, true)
    val NOK = Currency("NOK", "kr", ',', ' ', SymbolPosition.SUFFIX, true)
    val DKK = Currency("DKK", "kr", ',', '.', SymbolPosition.SUFFIX, true)
    val PLN = Currency("PLN", "zł", ',', ' ', SymbolPosition.SUFFIX, true)
    val CZK = Currency("CZK", "Kč", ',', ' ', SymbolPosition.SUFFIX, true)
    val TRY = Currency("TRY", "₺", ',', '.', SymbolPosition.PREFIX, false)
    val RUB = Currency("RUB", "₽", ',', ' ', SymbolPosition.SUFFIX, true)
    val INR = Currency("INR", "₹", '.', ',', SymbolPosition.PREFIX, false)
    val KRW = Currency("KRW", "₩", '.', ',', SymbolPosition.PREFIX, false, fractionDigits = 0)
    val SGD = Currency("SGD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val HKD = Currency("HKD", "$", '.', ',', SymbolPosition.PREFIX, false)
    val ZAR = Currency("ZAR", "R", '.', ',', SymbolPosition.PREFIX, true)
    val AED = Currency("AED", "د.إ", '.', ',', SymbolPosition.PREFIX, true)
    val ILS = Currency("ILS", "₪", '.', ',', SymbolPosition.PREFIX, false)

    val SUPPORTED: Map<String, Currency> =
        listOf(
            BRL,
            USD,
            EUR,
            GBP,
            JPY,
            CNY,
            ARS,
            MXN,
            CLP,
            COP,
            PEN,
            UYU,
            CAD,
            AUD,
            NZD,
            CHF,
            SEK,
            NOK,
            DKK,
            PLN,
            CZK,
            TRY,
            RUB,
            INR,
            KRW,
            SGD,
            HKD,
            ZAR,
            AED,
            ILS,
        ).associateBy { it.code }

    private val COUNTRY_TO_CURRENCY: Map<String, Currency> =
        mapOf(
            "BR" to BRL,
            "US" to USD,
            "PR" to USD,
            "EC" to USD,
            "SV" to USD,
            "PA" to USD,
            "DE" to EUR,
            "FR" to EUR,
            "IT" to EUR,
            "ES" to EUR,
            "PT" to EUR,
            "NL" to EUR,
            "BE" to EUR,
            "AT" to EUR,
            "IE" to EUR,
            "FI" to EUR,
            "GR" to EUR,
            "LU" to EUR,
            "SK" to EUR,
            "SI" to EUR,
            "EE" to EUR,
            "LV" to EUR,
            "LT" to EUR,
            "MT" to EUR,
            "CY" to EUR,
            "HR" to EUR,
            "GB" to GBP,
            "JP" to JPY,
            "CN" to CNY,
            "AR" to ARS,
            "MX" to MXN,
            "CL" to CLP,
            "CO" to COP,
            "PE" to PEN,
            "UY" to UYU,
            "CA" to CAD,
            "AU" to AUD,
            "NZ" to NZD,
            "CH" to CHF,
            "SE" to SEK,
            "NO" to NOK,
            "DK" to DKK,
            "PL" to PLN,
            "CZ" to CZK,
            "TR" to TRY,
            "RU" to RUB,
            "IN" to INR,
            "KR" to KRW,
            "SG" to SGD,
            "HK" to HKD,
            "ZA" to ZAR,
            "AE" to AED,
            "IL" to ILS,
        )

    fun byCode(code: String): Currency = SUPPORTED[code.uppercase()] ?: BRL

    fun byCountry(countryCode: String): Currency = COUNTRY_TO_CURRENCY[countryCode.uppercase()] ?: BRL
}
