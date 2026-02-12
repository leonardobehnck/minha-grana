package com.minhagrana.ui.components

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
import com.minhagrana.ui.theme.AppTheme

@Composable
fun Header1(
    modifier: Modifier = Modifier,
    title: String = "",
    actionText: String = "",
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, bottom = 16.dp),
    ) {
        Row {
            Text(
                modifier =
                    Modifier
                        .weight(1f),
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.headlineLarge,
            )
            if (actionText != "") {
                Text(
                    modifier =
                        Modifier
                            .padding(end = 16.dp, bottom = 2.dp)
                            .align(Alignment.Bottom)
                            .noRippleClickable { onClick() },
                    text = actionText,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
fun Header1Preview() {
    AppTheme {
        Header1(
            title = "Meu Dinheiro",
            actionText = "link",
        )
    }
}
