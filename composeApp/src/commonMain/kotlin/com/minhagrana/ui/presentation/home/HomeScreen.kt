package com.minhagrana.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Month
import com.minhagrana.entities.Year
import com.minhagrana.models.home.HomeInteraction
import com.minhagrana.models.home.HomeViewModel
import com.minhagrana.models.home.HomeViewState
import com.minhagrana.ui.components.Balance
import com.minhagrana.ui.components.CircularIcon
import com.minhagrana.ui.components.Header3
import com.minhagrana.ui.components.PieChart
import com.minhagrana.ui.processMonthDataByExpense
import com.minhagrana.util.currentMonthNumber
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onProfileSelected: () -> Unit,
    viewModel: HomeViewModel = koinInject()
) {
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.interact(HomeInteraction.OnScreenOpened)
    }

    when (val currentState = state) {
        is HomeViewState.Idle,
        is HomeViewState.Loading,
        -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeViewState.Success -> {
            HomeContent(
                onProfileSelected = onProfileSelected,
                year = currentState.year,
                userName = currentState.userName,
            )
        }

        is HomeViewState.Error -> {
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

        is HomeViewState.NoConnection -> {
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
private fun HomeContent(
    onProfileSelected: () -> Unit,
    year: Year,
    userName: String,
) {
    val currentMonthIndex = currentMonthNumber() - 1
    val month = year.months.getOrNull(currentMonthIndex) ?: year.months.firstOrNull() ?: Month()
    val totalBalance = year.months.sumOf { it.balance }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        Header3(
            title = userName,
            onProfileSelected = onProfileSelected,
        )
        Balance(
            balanceValue = totalBalance,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .verticalScroll(rememberScrollState()),
            ) {
                val expenseByCategory = processMonthDataByExpense(month)
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "relatório do mês",
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    PieChart(
                        reportItems = expenseByCategory,
                    )
                }
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    expenseByCategory.keys.forEach { category ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                        ) {
                            CircularIcon(color = category.color)
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = category.name,
                            )
                        }
                    }
                }
            }
        }
    }
}
