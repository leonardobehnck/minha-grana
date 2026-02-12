package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> GenericDialog(
    title: String,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    onDismissRequest: () -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(8.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Header1(title = title)
            LazyColumn(
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                items(items.size) { index ->
                    val item = items[index]
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .clip(RoundedCornerShape(8.dp))
                                .padding(16.dp)
                                .noRippleClickable { onItemSelected(item) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        itemContent(item)

                        Icon(
                            painter = painterResource(Res.drawable.ic_profile),
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp)
                                    .align(Alignment.CenterVertically),
                        )
                    }
                }
            }
        }
    }
}
