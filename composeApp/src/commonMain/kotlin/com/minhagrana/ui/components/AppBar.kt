package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "",
    actionText: String = "",
    actionIcon: ImageVector? = null,
    navigateUp: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onClickAction: () -> Unit = {},
) = TopAppBar(
    colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
        ),
    modifier =
        Modifier
            .background(backgroundColor),
    title =
        {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .height(56.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .background(backgroundColor)
                            .align(CenterVertically),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier =
                            Modifier
                                .noRippleClickable { navigateUp() },
                    )
                }
                Text(
                    modifier =
                        Modifier
                            .padding(start = 10.dp)
                            .align(CenterVertically)
                            .weight(1f),
                    text = title,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displaySmall,
                )
                if (actionIcon != null || actionText != "") {
                    Text(
                        modifier =
                            Modifier
                                .noRippleClickable { onClickAction() }
                                .padding(end = 16.dp)
                                .align(CenterVertically),
                        text = actionText,
                        maxLines = 1,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (actionIcon != null) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = "delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier =
                                Modifier
                                    .noRippleClickable { onClickAction() }
                                    .padding(end = 16.dp)
                                    .align(CenterVertically),
                        )
                    }
                }
            }
        },
)

@Preview
@Composable
fun PreviewAppBarWithBackButton() {
    AppTheme {
        AppBar(
            title = "Editar",
            navigateUp = {},
        )
    }
}
