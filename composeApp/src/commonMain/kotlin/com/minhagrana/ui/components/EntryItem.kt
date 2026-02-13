package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.ui.formatDoubleToBRL
import com.minhagrana.ui.theme.AppTheme

@Composable
fun EntryItem(
    entry: Entry = Entry(),
    onClick: () -> Unit = {},
) {
    val color =
        when (entry.type) {
            EntryType.EXPENSE -> MaterialTheme.colorScheme.error
            EntryType.INCOME -> MaterialTheme.colorScheme.primary
        }

    Column(
        modifier = Modifier.clickable { onClick() },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                CircularIcon(
                    color = entry.category.color,
                )
            }

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
            ) {
                Text(
                    text = entry.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = entry.category.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            Column {
                Text(
                    text = formatDoubleToBRL(entry.value),
                    color = color,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            Column {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver detalhes.",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

@Preview
@Composable
fun PreviewEntryItem() {
    AppTheme {
        EntryItem()
    }
}
