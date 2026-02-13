package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@Composable
fun MonthChanger(
    month: String,
    onNextPressed: () -> Unit,
    onPreviousPressed: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .height(48.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier =
                Modifier
                    .padding(horizontal = 8.dp)
                    .size(25.dp)
                    .noRippleClickable { onPreviousPressed() },
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = "Previous Month",
        )
        Text(
            text = month,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        Icon(
            modifier =
                Modifier
                    .padding(horizontal = 8.dp)
                    .size(25.dp)
                    .noRippleClickable { onNextPressed() },
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = "Next Month",
        )
    }
}

@Preview
@Composable
fun PreviewMonthChanger() {
    AppTheme {
        MonthChanger(
            onNextPressed = {},
            onPreviousPressed = {},
            month = "Janeiro",
        )
    }
}
