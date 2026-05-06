package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.currency.LocalCurrency
import com.minhagrana.currency.format
import com.minhagrana.ui.balanceColor
import com.minhagrana.ui.theme.Elevation

@Composable
fun BalanceCard(
    title: String = "",
    subtitle: String = "",
    balanceValue: Double = 4000.0,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = Elevation.raised,
                pressedElevation = Elevation.card,
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
                    text = format(balanceValue, LocalCurrency.current),
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
}
