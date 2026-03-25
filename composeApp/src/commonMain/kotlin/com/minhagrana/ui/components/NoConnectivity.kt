package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.back
import minhagrana.composeapp.generated.resources.no_connection
import minhagrana.composeapp.generated.resources.no_connection_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoConnectivity(onClick: (() -> Unit)) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(Res.string.no_connection),
            maxLines = 3,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.displaySmall,
        )

        Spacer(Modifier.height(14.dp))

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(Res.string.no_connection_message),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            title = stringResource(Res.string.back),
            onClick = onClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoConnectionView() {
    NoConnectivity(onClick = {})
}
