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
import com.minhagrana.ui.theme.gray

@Composable
fun Header2(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String = "",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(141.dp)
                .padding(start = 16.dp, top = 46.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = subtitle,
            color = gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview
@Composable
fun Header2Preview() {
    AppTheme {
        Header2(
            title = "Esqueci minha senha",
            subtitle = "Informe o email cadastrado.",
        )
    }
}
