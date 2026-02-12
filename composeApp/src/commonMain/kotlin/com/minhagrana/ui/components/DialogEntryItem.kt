package com.minhagrana.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.getCurrentDate
import com.minhagrana.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEntryItem(
    title: String = "Novo lançamento",
    showBottomSheet: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirmSelected: () -> Unit = {},
) {
    var entryName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedRepeat by remember { mutableIntStateOf(0) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val openCategoryDialog = remember { mutableStateOf(false) }
    val openRepeatDialog = remember { mutableStateOf(false) }

    var selectedEntryPositive by rememberSaveable { mutableStateOf(true) }
    var selectedEntryNegative by rememberSaveable { mutableStateOf(false) }

    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(""),
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier =
                Modifier
                    .wrapContentHeight(),
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BasicInputText(
                    modifier = Modifier.padding(start = 16.dp),
                    hint = title,
                    value = entryName,
                    onValueChange = { entryName = it },
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                )
                Crossfade(
                    targetState = !selectedEntryPositive,
                    animationSpec =
                        TweenSpec(
                            durationMillis = 700,
                            easing = FastOutSlowInEasing,
                        ),
                    label = "",
                ) {
                    AnimatedVisibility(
                        visible = selectedEntryPositive || selectedEntryNegative,
                    ) {
                        Row {
                            SelectorEntry(
                                title = "Entrada",
                                selected = selectedEntryPositive,
                                textColor = MaterialTheme.colorScheme.primary,
                                onClick = {
                                    selectedEntryPositive = true
                                    selectedEntryNegative = false
                                },
                            )
                            SelectorEntry(
                                title = "Saída",
                                selected = selectedEntryNegative,
                                textColor = MaterialTheme.colorScheme.error,
                                onClick = {
                                    selectedEntryNegative = true
                                    selectedEntryPositive = false
                                },
                            )
                        }
                    }
                }
                InputText(
                    title = "Valor",
                    maxLength = 11,
                    textFieldValue = value,
                    onValueChange = {
                        value =
                            it.copy(
                                selection = TextRange(it.text.length),
                            )
                    },
                    visualTransformation = BRLVisualTransformation(),
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                    hint = "R$0,00",
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .weight(1f),
                    ) {
                        PrimaryButton(
                            title = "Cancelar",
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    onDismiss()
                                }
                            },
                        )
                    }
                    Column(
                        modifier =
                            Modifier
                                .weight(1f),
                    ) {
                        SecondaryButton(
                            title = "Adicionar",
                            onClick = onConfirmSelected,
                        )
                    }
                }
                Column {
                    DatePicker(onDateSelected = { selectedDate = it })
                    Link(
                        title = "Categoria",
                        iconRightVisibility = true,
                        result = selectedCategory,
                        color = MaterialTheme.colorScheme.onSecondary,
                        onClick = { openCategoryDialog.value = true },
                    )
                    Link(
                        title = "Repetir",
                        iconRightVisibility = true,
                        result = if (selectedRepeat == 0) "" else "${selectedRepeat}x",
                        color = MaterialTheme.colorScheme.onSecondary,
                        onClick = { openRepeatDialog.value = true },
                    )
                }
            }
        }
        when {
            openCategoryDialog.value ->
                DialogCategory(
                    onItemSelected = {
                        selectedCategory = it.name
                        openCategoryDialog.value = false
                    },
                    onDismissRequest = { openCategoryDialog.value = false },
                )

            openRepeatDialog.value ->
                DialogRepeat(
                    onItemSelected = {
                        selectedRepeat = it
                        openRepeatDialog.value = false
                    },
                    onDismissRequest = { openRepeatDialog.value = false },
                )
        }
    }
}

@Preview
@Composable
fun DialogEntryItemPreview() {
    AppTheme {
        DialogEntryItem(
            title = "Tem certeza que deseja deletar?",
            onDismiss = {},
            showBottomSheet = true,
        )
    }
}
