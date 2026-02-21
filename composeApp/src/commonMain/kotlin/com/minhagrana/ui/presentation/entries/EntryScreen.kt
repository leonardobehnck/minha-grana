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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Entry
import com.minhagrana.models.entries.EntryInteraction
import com.minhagrana.models.entries.EntryViewModel
import com.minhagrana.models.entries.EntryViewState
import com.minhagrana.ui.components.AppBar
import com.minhagrana.ui.components.DatePicker
import com.minhagrana.ui.components.Dialog
import com.minhagrana.ui.components.DialogCategory
import com.minhagrana.ui.components.EditItemHeader
import com.minhagrana.ui.components.Error
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.Link
import com.minhagrana.ui.components.NoConnectivity
import com.minhagrana.ui.components.ProgressBar
import com.minhagrana.ui.components.SecondaryButton
import com.minhagrana.ui.parseBRLInputToDouble
import org.koin.compose.koinInject

@Composable
fun EntryScreen(
    entryUuid: String,
    navigateUp: () -> Unit,
    onSaveEntrySelected: () -> Unit,
    onEntryDeleted: () -> Unit,
    viewModel: EntryViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    when (val currentState = state) {
        is EntryViewState.Idle -> {
            viewModel.interact(EntryInteraction.OnScreenOpened(entryUuid))
        }

        is EntryViewState.Loading,
        -> {
            ProgressBar()
        }

        is EntryViewState.Success -> {
            EntryContent(
                entry = currentState.entry,
                navigateUp = navigateUp,
                onSaveEntry = { updatedEntry ->
                    viewModel.interact(EntryInteraction.OnEntryUpdated(updatedEntry))
                    onSaveEntrySelected()
                },
                onDeleteEntry = {
                    viewModel.interact(EntryInteraction.OnEntryDeleted)
                    onEntryDeleted()
                },
            )
        }

        is EntryViewState.Error -> {
            Error(message = currentState.message)
        }

        is EntryViewState.NoConnection -> {
            NoConnectivity {
                viewModel.interact(EntryInteraction.OnScreenOpened(entryUuid))
            }
        }
    }
}

@Composable
private fun EntryContent(
    entry: Entry,
    navigateUp: () -> Unit,
    onSaveEntry: (Entry) -> Unit,
    onDeleteEntry: () -> Unit,
) {
    var category by remember { mutableStateOf(entry.category) }
    var entryNameValue by remember { mutableStateOf(entry.name) }
    var entryValue by remember {
        mutableStateOf(
            kotlin.math
                .round(entry.value * 100)
                .toLong()
                .toString(),
        )
    }
    var entryCategory by remember { mutableStateOf(entry.category.name) }
    var entryDate by remember { mutableStateOf(entry.date) }
    var entryType by remember { mutableStateOf(entry.type) }

    val showBottomSheetDelete = remember { mutableStateOf(false) }
    val showBottomSheetCategory = remember { mutableStateOf(false) }

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
                        .background(MaterialTheme.colorScheme.surface),
            ) {
                EditItemHeader(
                    entry = entry.copy(type = entryType),
                    category = category,
                    name = entryNameValue,
                    entryNameValue = entryNameValue,
                    onEntryNameChange = { newName -> entryNameValue = newName },
                    newValue = entryValue,
                    onEntryValueChange = { newValue -> entryValue = newValue },
                )
                Column(
                    modifier = Modifier.padding(start = 48.dp),
                ) {
                    DatePicker(
                        selectedDate = entryDate,
                        onDateSelected = { entryDate = it },
                    )
                    Link(
                        title = "Categoria",
                        iconRightVisibility = true,
                        result = entryCategory,
                        color = MaterialTheme.colorScheme.onSecondary,
                        onClick = { showBottomSheetCategory.value = true },
                    )
                }
            }
            Dialog(
                title = "Deletar este lançamento?",
                subtitle = "Tem certeza que deseja excluir?",
                actionButtonText = "Sim",
                dismissButtonText = "Cancelar",
                onConfirmSelected = {
                    showBottomSheetDelete.value = false
                    onDeleteEntry()
                },
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
            }
        }
        SecondaryButton(
            title = "Salvar",
            onClick = {
                val updatedEntry =
                    entry.copy(
                        name = entryNameValue,
                        value = parseBRLInputToDouble(entryValue),
                        date = entryDate,
                        type = entryType,
                        category = category,
                    )
                onSaveEntry(updatedEntry)
            },
        )
    }
}
