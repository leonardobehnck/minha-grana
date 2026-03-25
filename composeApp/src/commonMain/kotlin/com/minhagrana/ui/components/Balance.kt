package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.balanceColor
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.balance_hidden
import minhagrana.composeapp.generated.resources.hide_balance
import minhagrana.composeapp.generated.resources.ic_hide
import minhagrana.composeapp.generated.resources.ic_unhide
import minhagrana.composeapp.generated.resources.month_balance
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Balance(
    balanceValue: Double = 4000.0,
    balanceVisibility: Boolean = false,
) {
    var isBalanceVisible by remember { mutableStateOf(balanceVisibility) }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(85.dp)
                .background(MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = stringResource(Res.string.month_balance),
                )
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    color = balanceColor(balanceValue),
                    text = if (isBalanceVisible) formatDoubleToBRL(balanceValue) else stringResource(Res.string.balance_hidden),
                )
            }
            Icon(
                modifier = Modifier.noRippleClickable { isBalanceVisible = !isBalanceVisible },
                painter =
                    if (isBalanceVisible) {
                        painterResource(Res.drawable.ic_unhide)
                    } else {
                        painterResource(Res.drawable.ic_hide)
                    },
                contentDescription = stringResource(Res.string.hide_balance),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
fun PreviewBalance() {
    AppTheme {
        Balance(
            balanceValue = -15.0,
        )
    }
}
