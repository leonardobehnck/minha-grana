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
import com.minhagrana.entities.Entry
import com.minhagrana.entities.Month
import com.minhagrana.models.entries.EntriesInteraction
import com.minhagrana.models.entries.EntriesViewModel
import com.minhagrana.models.entries.EntriesViewState
import com.minhagrana.ui.components.BalanceItem
import com.minhagrana.ui.components.EntryItem
import com.minhagrana.ui.components.Error
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.MonthChanger
import com.minhagrana.ui.components.NoConnectivity
import com.minhagrana.ui.components.ProgressBar
import org.koin.compose.koinInject

@Composable
fun EntriesScreen(
    monthUuid: String? = null,
    yearId: Long = -1,
    onEntrySelected: (Entry) -> Unit,
    onEntriesByYearSelected: () -> Unit,
    viewModel: EntriesViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(monthUuid, yearId) {
        if (monthUuid != null) {
            viewModel.setCurrentMonth(monthUuid, yearId)
        }
        viewModel.interact(EntriesInteraction.OnMonthSelected)
    }

    when (val currentState = state) {
        is EntriesViewState.Idle,
        is EntriesViewState.Loading,
        -> {
            ProgressBar()
        }

        is EntriesViewState.Success -> {
            EntriesContent(
                month = currentState.month,
                onEntrySelected = onEntrySelected,
                onEntriesByYearSelected = onEntriesByYearSelected,
                onNextMonth = { viewModel.interact(EntriesInteraction.OnNextMonthSelected("")) },
                onPreviousMonth = { viewModel.interact(EntriesInteraction.OnPreviousYearSelected("")) },
            )
        }

        is EntriesViewState.Error -> {
            Error(message = currentState.message)
        }

        is EntriesViewState.NoConnection -> {
            NoConnectivity { viewModel.interact(EntriesInteraction.OnMonthSelected) }
        }
    }
}

@Composable
private fun EntriesContent(
    month: Month,
    onEntrySelected: (Entry) -> Unit,
    onEntriesByYearSelected: () -> Unit,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
        ) {
            Header1(
                title = "Meu balanço",
                actionText = "Ver balanço anual >",
                onClick = { onEntriesByYearSelected() },
            )
            MonthChanger(
                onNextPressed = onNextMonth,
                onPreviousPressed = onPreviousMonth,
                month = month.name,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxSize(),
            ) {
                if (month.entries.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Nenhum lançamento neste mês",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                } else {
                    month.entries.forEach { entry ->
                        EntryItem(
                            entry = entry,
                            onClick = { onEntrySelected(entry) },
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(top = 20.dp),
                ) {
                    BalanceItem(
                        month = month,
                    )
                }
            }
        }
    }
}
