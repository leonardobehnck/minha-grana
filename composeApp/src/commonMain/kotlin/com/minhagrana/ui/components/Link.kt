package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme
import com.minhagrana.ui.theme.gray

@Composable
fun Link(
    modifier: Modifier = Modifier,
    title: String = "",
    result: String = "",
    iconRightVisibility: Boolean = false,
    iconLeftVisibility: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .noRippleClickable(onClick = { onClick.invoke() }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconLeftVisibility) {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 1.dp)
                        .size(12.dp)
                        .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = color,
            )
        }
        Text(
            text = title,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
        if (iconRightVisibility) {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 1.dp)
                        .size(12.dp)
                        .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = color,
            )
        }
        Text(
            modifier =
                Modifier
                    .padding(horizontal = 10.dp),
            text = result,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = gray,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLink() {
    AppTheme {
        Link(
            title = "Description",
            iconRightVisibility = true,
            result = "Geral",
        )
    }
}
