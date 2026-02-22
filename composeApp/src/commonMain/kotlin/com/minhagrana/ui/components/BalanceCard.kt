package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme

@Composable
fun BalanceCard(
    title: String = "Outubro",
    balanceValue: Double = 4000.0,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() }
                .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 32.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = title,
        )

        Row(
            modifier =
                Modifier
                    .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(start = 32.dp)
                        .weight(1f),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (balanceValue > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    text = formatDoubleToBRL(balanceValue),
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Entradas",
                )
            }

            Icon(
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp)
                        .size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun PreviewBalanceCard() {
    AppTheme {
        BalanceCard(
            title = "Outubro",
        )
    }
}
