package com.minhagrana.ui.presentation.newentry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.currency.CurrencyVisualTransformation
import com.minhagrana.currency.LocalCurrency
import com.minhagrana.currency.format
import com.minhagrana.entities.NullableCategorySaver
import com.minhagrana.models.newentry.NewEntryFormState
import com.minhagrana.models.newentry.NewEntryInteraction
import com.minhagrana.models.newentry.NewEntryViewModel
import com.minhagrana.models.newentry.NewEntryViewState
import com.minhagrana.ui.components.BasicInputText
import com.minhagrana.ui.components.DatePicker
import com.minhagrana.ui.components.DialogCategory
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.Link
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.ProgressBar
import com.minhagrana.ui.components.SelectorEntry
import com.minhagrana.ui.components.localizedName
import com.minhagrana.ui.getCurrentDate
import com.minhagrana.ui.theme.Elevation
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.add
import minhagrana.composeapp.generated.resources.add_entry
import minhagrana.composeapp.generated.resources.category
import minhagrana.composeapp.generated.resources.expense
import minhagrana.composeapp.generated.resources.income
import minhagrana.composeapp.generated.resources.name
import minhagrana.composeapp.generated.resources.saving
import minhagrana.composeapp.generated.resources.value
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun NewEntryScreen(
    onEntrySaved: () -> Unit,
    onCreateCategory: () -> Unit = {},
    viewModel: NewEntryViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    when (state) {
        is NewEntryViewState.Loading -> {
            ProgressBar()
        }

        is NewEntryViewState.Success -> {
            NewEntryContent(
                isSaving = state is NewEntryViewState.Loading,
                errorMessage = (state as? NewEntryViewState.Error)?.message,
                onSaveClicked = { form -> viewModel.interact(NewEntryInteraction.OnSaveClicked(form)) },
                onCreateCategory = onCreateCategory,
            )
        }

        is NewEntryViewState.EntrySaved -> {
            LaunchedEffect(Unit) {
                onEntrySaved()
            }
        }

        is NewEntryViewState.Error -> {
            Text(text = (state as NewEntryViewState.Error).message)
        }

        is NewEntryViewState.Idle -> {
            viewModel.interact(NewEntryInteraction.OnScreenOpened)
        }
    }
}

@Composable
private fun NewEntryContent(
    isSaving: Boolean,
    errorMessage: String?,
    onSaveClicked: (NewEntryFormState) -> Unit,
    onCreateCategory: () -> Unit,
) {
    val openCategoryDialog = remember { mutableStateOf(false) }

    var entryName by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(getCurrentDate()) }
    var selectedCategory by rememberSaveable(stateSaver = NullableCategorySaver) { mutableStateOf(null) }
    var selectedEntryPositive by rememberSaveable { mutableStateOf(false) }
    var selectedEntryNegative by rememberSaveable { mutableStateOf(true) }

    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
        ) {
            Header1(
                title = stringResource(Res.string.add_entry),
            )
            Card(
                shape = MaterialTheme.shapes.large,
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = Elevation.raised,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    BasicInputText(
                        hint = stringResource(Res.string.name),
                        value = entryName,
                        onValueChange = { entryName = it },
                        keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Next,
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
                            Surface(
                                shape = MaterialTheme.shapes.large,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    SelectorEntry(
                                        title = stringResource(Res.string.income),
                                        selected = selectedEntryPositive,
                                        textColor = MaterialTheme.colorScheme.primary,
                                        onClick = {
                                            selectedEntryPositive = true
                                            selectedEntryNegative = false
                                        },
                                    )
                                    SelectorEntry(
                                        title = stringResource(Res.string.expense),
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
                    }

                    val currency = LocalCurrency.current
                    InputText(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.value),
                        maxLength = 11,
                        textFieldValue = value,
                        onValueChange = {
                            value = it.copy(selection = TextRange(it.text.length))
                        },
                        visualTransformation = CurrencyVisualTransformation(currency),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                            ),
                        hint = format(0.0, currency),
                    )

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            DatePicker(
                                selectedDate = selectedDate,
                                onDateSelected = { selectedDate = it },
                            )

                            Link(
                                title = stringResource(Res.string.category),
                                iconRightVisibility = true,
                                result = selectedCategory?.localizedName().orEmpty(),
                                color = MaterialTheme.colorScheme.onSecondary,
                                onClick = { openCategoryDialog.value = true },
                            )
                        }
                    }
                }
            }

            when {
                openCategoryDialog.value ->
                    DialogCategory(
                        onItemSelected = {
                            selectedCategory = it
                            openCategoryDialog.value = false
                        },
                        onDismissRequest = { openCategoryDialog.value = false },
                        onCreateNewCategory = {
                            openCategoryDialog.value = false
                            onCreateCategory()
                        },
                    )
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            PrimaryButton(
                title = if (isSaving) stringResource(Res.string.saving) else stringResource(Res.string.add),
                onClick = {
                    onSaveClicked(
                        NewEntryFormState(
                            name = entryName,
                            date = selectedDate,
                            category = selectedCategory,
                            isIncome = selectedEntryPositive,
                            valueText = value.text,
                        ),
                    )
                },
            )
        }
    }
}
