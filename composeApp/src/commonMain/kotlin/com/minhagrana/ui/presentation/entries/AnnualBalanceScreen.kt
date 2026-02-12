package com.minhagrana.ui.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Month
import com.minhagrana.entities.Year
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.BalanceCard
import com.minhagrana.ui.components.MonthChanger

@Composable
fun AnnualBalanceScreen(
    navigateUp: () -> Unit,
    onMonthSelected: () -> Unit,
) {
    val year =
        Year(
            name = "2023",
            months =
                listOf(
                    Month(
                        name = "Outubro",
                        income = 5000.0,
                        expense = 3000.0,
                        balance = 2000.0,
                    ),
                    Month(
                        name = "Novembro",
                        income = 5000.0,
                        expense = 3000.0,
                        balance = 2000.0,
                    ),
                ),
        )
    Scaffold(
        modifier =
            Modifier.background(MaterialTheme.colorScheme.background),
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
                onNextPressed = { /*TODO*/ },
                onPreviousPressed = { /*TODO*/ },
                month = "2024",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxSize(),
            ) {
                year.months.forEach { month ->
                    BalanceCard(
                        balanceValue = month.balance,
                        onClick = onMonthSelected,
                    )
                }
            }
        }
    }
}
