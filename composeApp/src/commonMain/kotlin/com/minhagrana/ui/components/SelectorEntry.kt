package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@Composable
fun SelectorEntry(
    title: String = "Entrada",
    textColor: Color,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val backgroundColor: Color =
        if (selected) {
            Color(0x30C2C2C2)
        } else {
            Color.Transparent
        }

    Surface(
        modifier =
            Modifier
                .noRippleClickable { onClick() }
                .padding(8.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp),
            text = title,
            color = textColor,
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Preview
@Composable
fun PreviewSelectorEntry() {
    AppTheme {
        Row {
            SelectorEntry(
                textColor = MaterialTheme.colorScheme.error,
                selected = true,
            )
            SelectorEntry(
                textColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
