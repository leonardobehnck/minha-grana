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
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme
import com.minhagrana.ui.theme.gray
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.ic_hide
import minhagrana.composeapp.generated.resources.ic_unhide
import org.jetbrains.compose.resources.painterResource

@Composable
fun Balance(
    balanceValue: Double = 4000.0,
    balanceVisibility: Boolean = true,
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
                    text = "saldo do mês",
                )
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (balanceValue > 0)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    text = if (isBalanceVisible) formatDoubleToBRL(balanceValue) else "R$ *****",
                )
            }
            Icon(
                modifier = Modifier.noRippleClickable { isBalanceVisible = !isBalanceVisible },
                painter =
                    if (isBalanceVisible) {
                        painterResource(Res.drawable.ic_hide)
                    } else {
                        painterResource(Res.drawable.ic_unhide)
                    },
                contentDescription = "hide balance",
                tint = gray,
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
