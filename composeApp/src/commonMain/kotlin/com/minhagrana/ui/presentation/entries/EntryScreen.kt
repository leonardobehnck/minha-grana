package com.minhagrana.ui.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.DatePicker
import com.minhagrana.ui.components.Dialog
import com.minhagrana.ui.components.DialogCategory
import com.minhagrana.ui.components.DialogRepeat
import com.minhagrana.ui.components.EditItemHeader
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.Link
import com.minhagrana.ui.components.SecondaryButton
import com.minhagrana.ui.theme.AppTheme

@Composable
fun EntryScreen(
    navigateUp: () -> Unit = {},
    onSaveEntrySelected: () -> Unit = {},
) {
    val entry = Entry()
    var category by remember { mutableStateOf(Category()) }

    var entryNameValue by remember { mutableStateOf("") }
    var entryValue by remember { mutableStateOf(entry.value.toString()) }
    var entryCategory by remember { mutableStateOf(entry.category.name) }
    var entryDate by remember { mutableStateOf(entry.date) }
    var entryRepeat by remember { mutableIntStateOf(entry.repeat) }

    val showBottomSheetDelete = remember { mutableStateOf(false) }
    val showBottomSheetCategory = remember { mutableStateOf(false) }
    val showBottomSheetRepeat = remember { mutableStateOf(false) }

    Column {
        AppBar(
            actionIcon = Icons.Default.Delete,
            navigateUp = { navigateUp() },
            onClickAction = { showBottomSheetDelete.value = true },
        )
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
        ) {
            Header1(
                title = "Editar lançamento",
            )
            Column(
                modifier =
                    Modifier
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer),
            ) {
                EditItemHeader(
                    entry = entry,
                    category = category,
                    name = entry.name,
                    entryNameValue = entryNameValue,
                    onEntryNameChange = { newName -> entryNameValue = newName },
                    newValue = entryValue,
                    onEntryValueChange = { newValue -> entryValue = newValue },
                )
                Column(
                    modifier = Modifier.padding(start = 48.dp),
                ) {
                    DatePicker(onDateSelected = { entryDate = it })
                    Link(
                        title = "Categoria",
                        iconRightVisibility = true,
                        result = entryCategory,
                        color = MaterialTheme.colorScheme.onSecondary,
                        onClick = { showBottomSheetCategory.value = true },
                    )
                    Link(
                        title = "Repetir",
                        iconRightVisibility = true,
                        result = if (entryRepeat == 0) "" else "${entryRepeat}x",
                        color = MaterialTheme.colorScheme.onSecondary,
                        onClick = { showBottomSheetRepeat.value = true },
                    )
                }
            }
            Dialog(
                title = "Deletar este lançamento?",
                subtitle = "Tem certeza que deseja excluir?",
                actionButtonText = "Sim",
                dismissButtonText = "Cancelar",
                onConfirmSelected = {},
                onDismiss = { showBottomSheetDelete.value = false },
                showBottomSheet = showBottomSheetDelete.value,
            )
            when {
                showBottomSheetCategory.value ->
                    DialogCategory(
                        onDismissRequest = { showBottomSheetCategory.value = false },
                        onItemSelected = {
                            category = it
                            entryCategory = it.name
                            showBottomSheetCategory.value = false
                        },
                    )

                showBottomSheetRepeat.value ->
                    DialogRepeat(
                        onDismissRequest = { showBottomSheetRepeat.value = false },
                        onItemSelected = {
                            entryRepeat = it
                            showBottomSheetRepeat.value = false
                        },
                    )
            }
        }
        SecondaryButton(
            title = "Salvar",
            onClick = { onSaveEntrySelected() },
        )
    }
}

@Preview
@Composable
fun PreviewEntryScreen() {
    AppTheme {
        EntryScreen()
    }
}
