package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.ui.theme.AppTheme

@Composable
fun EditItemHeader(
    entry: Entry,
    name: String = "Insira um nome",
    newValue: String,
    entryNameValue: String,
    onEntryNameChange: (String) -> Unit,
    onEntryValueChange: (String) -> Unit = {},
    category: Category = Category(),
) {
    val entryColor =
        when (entry.type) {
            EntryType.EXPENSE -> MaterialTheme.colorScheme.error
            EntryType.INCOME -> MaterialTheme.colorScheme.primary
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                CircularIcon(
                    modifier = Modifier.size(30.dp),
                    color = category.color,
                )
            }
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
            ) {
                BasicInputText(
                    modifier = Modifier.padding(start = 8.dp),
                    hint = name,
                    value = entryNameValue,
                    onValueChange = onEntryNameChange,
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                )
            }
        }
        BasicInputText(
            modifier = Modifier.padding(start = 48.dp, top = 16.dp),
            hint = "",
            maxLength = 12,
            value = newValue,
            onValueChange = onEntryValueChange,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            visualTransformation = BRLVisualTransformation(),
            textColor = entryColor,
        )
    }
}

@Preview
@Composable
fun PreviewEditItemHeader() {
    AppTheme {
        EditItemHeader(
            entryNameValue = "",
            onEntryNameChange = {},
            onEntryValueChange = {},
            name = "Insira um nome",
            newValue = "",
            entry = Entry(),
        )
    }
}
