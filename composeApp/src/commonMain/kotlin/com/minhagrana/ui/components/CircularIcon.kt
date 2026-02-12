package com.minhagrana.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularIcon(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSecondary,
) {
    Canvas(modifier = modifier.size(35.dp)) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2,
        )
    }
}

@Preview
@Composable
fun CircularIconPreview() {
    CircularIcon()
}
