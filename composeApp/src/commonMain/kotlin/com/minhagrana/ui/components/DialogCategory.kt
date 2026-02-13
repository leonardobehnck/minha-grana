package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Categories
import com.minhagrana.entities.Category
import com.minhagrana.repository.CategoryRepository
import org.koin.compose.koinInject

@Composable
fun DialogCategory(
    onItemSelected: (Category) -> Unit,
    onDismissRequest: () -> Unit,
    categoryRepository: CategoryRepository = koinInject(),
) {
    val categoriesFlow by categoryRepository.getAllCategoriesFlow().collectAsState(initial = emptyList())

    val categories = categoriesFlow.ifEmpty { Categories().categories }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(8.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
        ) {
            Header1(
                title = "Selecionar categoria",
            )
            if (categories.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 16.dp),
                ) {
                    items(categories.size) { index ->
                        Row(
                            modifier =
                                Modifier
                                    .noRippleClickable { onItemSelected(categories[index]) }
                                    .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularIcon(
                                modifier = Modifier.size(25.dp),
                                color = categories[index].color,
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = categories[index].name,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}
