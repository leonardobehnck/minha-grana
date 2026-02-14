package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@Composable
fun Header4(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 16.dp, top = 46.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
fun Header4Preview() {
    AppTheme {
        Header1(
            title = "Meu Dinheiro",
        )
    }
}
