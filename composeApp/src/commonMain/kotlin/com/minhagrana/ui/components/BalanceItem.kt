package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Month
import com.minhagrana.ui.balanceColor
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme

@Composable
fun BalanceItem(month: Month = Month()) {
    Column(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surface)
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
                    text = formatDoubleToBRL(month.income),
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Entradas",
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
                    text = formatDoubleToBRL(month.expense),
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Saídas",
                )
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
