package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    title: String,
    leadIcon: Boolean = false,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .noRippleClickable { onClick() }
                .padding(start = 64.dp, top = 16.dp, end = 16.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            if (leadIcon) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewListItem() {
    AppTheme {
        Column {
            ListItem(
                title = "Categoria",
                leadIcon = true,
                subtitle = "Transporte",
            )
            ListItem(
                title = "Categoria",
                leadIcon = false,
                subtitle = "Transporte",
            )
        }
    }
}
