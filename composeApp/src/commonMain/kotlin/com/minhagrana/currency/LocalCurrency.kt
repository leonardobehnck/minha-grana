package com.minhagrana.currency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.compose.koinInject

val LocalCurrency = staticCompositionLocalOf { Currencies.BRL }

@Composable
fun ProvideCurrency(
    repository: CurrencyRepository = koinInject(),
    content: @Composable () -> Unit,
) {
    val currency by repository.observe().collectAsState()
    CompositionLocalProvider(LocalCurrency provides currency, content = content)
}

@Composable
fun hiddenBalanceText(): String = "${LocalCurrency.current.symbol} *****"
