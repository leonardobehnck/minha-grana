package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.currency.LocalCurrency
import com.minhagrana.currency.format
import com.minhagrana.entities.Month
import com.minhagrana.ui.balanceColor
import com.minhagrana.ui.theme.AppTheme
import com.minhagrana.ui.theme.Elevation
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.expenses_label
import minhagrana.composeapp.generated.resources.incomes_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalanceItem(month: Month = Month()) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = Elevation.raised,
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .padding(top = 16.dp, bottom = 16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(start = 32.dp)
                            .weight(1f),
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        color = balanceColor(month.income),
                        text = format(month.income, LocalCurrency.current),
                    )
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(Res.string.incomes_label),
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .weight(1f),
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (month.expense == 0.0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                        text = format(month.expense, LocalCurrency.current),
                    )
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(Res.string.expenses_label),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBalanceItem() {
    AppTheme {
        BalanceItem()
    }
}
