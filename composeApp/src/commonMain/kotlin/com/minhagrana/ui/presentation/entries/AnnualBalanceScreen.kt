package com.minhagrana.ui.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.annualbalance.AnnualBalanceInteraction
import com.minhagrana.annualbalance.AnnualBalanceViewModel
import com.minhagrana.annualbalance.AnnualBalanceViewState
import com.minhagrana.entities.Month
import com.minhagrana.entities.Year
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.BalanceCard
import com.minhagrana.ui.components.MonthChanger
import org.koin.compose.koinInject

@Composable
fun AnnualBalanceScreen(
    navigateUp: () -> Unit,
    onMonthSelected: (Month) -> Unit,
    viewModel: AnnualBalanceViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.interact(AnnualBalanceInteraction.OnScreenOpened)
    }

    when (val currentState = state) {
        is AnnualBalanceViewState.Idle,
        is AnnualBalanceViewState.Loading,
        -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is AnnualBalanceViewState.Success -> {
            AnnualBalanceContent(
                year = currentState.year,
                navigateUp = navigateUp,
                onMonthSelected = onMonthSelected,
                onNextYear = { viewModel.interact(AnnualBalanceInteraction.OnNextYearSelected) },
                onPreviousYear = { viewModel.interact(AnnualBalanceInteraction.OnPreviousYearSelected) },
            )
        }

        is AnnualBalanceViewState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        is AnnualBalanceViewState.NoConnection -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun AnnualBalanceContent(
    year: Year,
    navigateUp: () -> Unit,
    onMonthSelected: (Month) -> Unit,
    onNextYear: () -> Unit,
    onPreviousYear: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            AppBar(
                navigateUp = navigateUp,
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
        ) {
            MonthChanger(
                onNextPressed = onNextYear,
                onPreviousPressed = onPreviousYear,
                month = year.name,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxSize(),
            ) {
                if (year.months.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Nenhum mês encontrado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                } else {
                    year.months.forEach { month ->
                        BalanceCard(
                            title = month.name,
                            balanceValue = month.balance,
                            onClick = { onMonthSelected(month) },
                        )
                    }
                }
            }
        }
    }
}
