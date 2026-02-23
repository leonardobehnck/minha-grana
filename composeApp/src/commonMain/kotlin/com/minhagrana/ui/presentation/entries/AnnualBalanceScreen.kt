package com.minhagrana.ui.presentation.entries

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.minhagrana.entities.Year
import com.minhagrana.models.annualbalance.AnnualBalanceInteraction
import com.minhagrana.models.annualbalance.AnnualBalanceViewModel
import com.minhagrana.models.annualbalance.AnnualBalanceViewState
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.BalanceCard
import com.minhagrana.ui.components.Error
import com.minhagrana.ui.components.MonthChanger
import com.minhagrana.ui.components.NoConnectivity
import com.minhagrana.ui.components.ProgressBar
import org.koin.compose.koinInject

@Composable
fun AnnualBalanceScreen(
    navigateUp: () -> Unit,
    onMonthSelected: (String, Long) -> Unit,
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
            ProgressBar()
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
            Error(message = currentState.message)
        }

        is AnnualBalanceViewState.NoConnection -> {
            NoConnectivity { viewModel.interact(AnnualBalanceInteraction.OnScreenOpened) }
        }
    }
}

@Composable
private fun AnnualBalanceContent(
    year: Year,
    navigateUp: () -> Unit,
    onMonthSelected: (String, Long) -> Unit,
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
            AnimatedContent(
                targetState = year,
                transitionSpec = {
                    val slideDirection =
                        if ((
                                targetState.name.toIntOrNull()
                                    ?: 0
                            ) >= (initialState.name.toIntOrNull() ?: 0)
                        ) {
                            1
                        } else {
                            -1
                        }
                    (
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth * slideDirection },
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                        ) + fadeIn(animationSpec = tween(300))
                    ).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth * slideDirection },
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                        ) + fadeOut(animationSpec = tween(300)),
                    )
                },
                label = "year_content",
            ) { targetYear ->
                Column(
                    modifier =
                        Modifier
                            .padding(bottom = 30.dp)
                            .fillMaxSize(),
                ) {
                    if (targetYear.months.isEmpty()) {
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
                        targetYear.months.forEach { month ->
                            BalanceCard(
                                title = month.name,
                                subtitle = "saldo do mês",
                                balanceValue = month.balance,
                                onClick = { onMonthSelected(month.uuid, targetYear.id.toLong()) },
                            )
                        }
                    }
                }
            }
        }
    }
}
