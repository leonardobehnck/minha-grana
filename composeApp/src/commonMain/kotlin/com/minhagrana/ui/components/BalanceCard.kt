package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.minhagrana.ui.balanceColor
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme

@Composable
fun BalanceCard(
    title: String = "Outubro",
    subtitle: String = "Entradas",
    balanceValue: Double = 4000.0,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() }
                .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                modifier = Modifier.padding(start = 32.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = title,
            )
            Text(
                modifier = Modifier.padding(start = 32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                text = subtitle,
            )
        }
        Row(
            modifier =
                Modifier
                    .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                color = balanceColor(balanceValue),
                text = formatDoubleToBRL(balanceValue),
            )
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
