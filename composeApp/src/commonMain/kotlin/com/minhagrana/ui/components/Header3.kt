package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun Header3(
    onProfileSelected: () -> Unit = {},
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier =
            modifier
                .fillMaxWidth()
                .height(100.dp)
                .noRippleClickable{ onProfileSelected() }
                .background(MaterialTheme.colorScheme.primary),
    ) {
        Row {
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                text = "Olá, $title!",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )
            Icon(
                painter = painterResource(Res.drawable.ic_profile),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Profile",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
fun Header3Preview() {
    AppTheme {
        Header3(
            title = "Leonardo",
        )
    }
}
