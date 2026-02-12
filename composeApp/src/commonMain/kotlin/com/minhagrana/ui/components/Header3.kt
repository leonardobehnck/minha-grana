package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@Composable
fun Header3(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier =
            modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colorScheme.primary),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Olá, $title!",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
        )
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
