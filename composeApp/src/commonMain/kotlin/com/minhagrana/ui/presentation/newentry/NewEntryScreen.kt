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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.YearRepository
import com.minhagrana.ui.components.BRLVisualTransformation
import com.minhagrana.ui.components.BasicInputText
import com.minhagrana.ui.components.DatePicker
import com.minhagrana.ui.components.DialogCategory
import com.minhagrana.ui.components.DialogRepeat
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.InputText
import com.minhagrana.ui.components.Link
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.components.SecondaryButton
import com.minhagrana.ui.components.SelectorEntry
import com.minhagrana.ui.getCurrentDate
import com.minhagrana.ui.parseBRLInputToDouble
import com.minhagrana.util.currentMonthNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun NewEntryScreen(
    onEntrySaved: () -> Unit = {},
    onCancel: () -> Unit = {},
    databaseInitializer: DatabaseInitializer = koinInject(),
    yearRepository: YearRepository = koinInject(),
    entryRepository: EntryRepository = koinInject(),
) {
    var entryName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedRepeat by remember { mutableIntStateOf(1) }
    var selectedEntryPositive by rememberSaveable { mutableStateOf(false) }
    var selectedEntryNegative by rememberSaveable { mutableStateOf(true) }
    var currentMonthId by remember { mutableStateOf<Long?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val openCategoryDialog = remember { mutableStateOf(false) }
    val openRepeatDialog = remember { mutableStateOf(false) }

    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val scope = rememberCoroutineScope()

    // Load current month ID on screen open
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val user = databaseInitializer.initialize()
            val year = yearRepository.getCurrentYearOrCreate(user.id.toLong())
            val currentMonthIndex = currentMonthNumber() - 1
            val month = year.months.getOrNull(currentMonthIndex)
            currentMonthId = month?.id?.toLong()
        }
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
                title = "Novo lançamento",
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BasicInputText(
                    hint = "Nome do lançamento",
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

                DatePicker(onDateSelected = { selectedDate = it })

                Link(
                    title = "Categoria",
                    iconRightVisibility = true,
                    result = selectedCategory?.name ?: "",
                    color = MaterialTheme.colorScheme.onSecondary,
                    onClick = { openCategoryDialog.value = true },
                )

                Link(
                    title = "Repetir",
                    iconRightVisibility = true,
                    result = if (selectedRepeat <= 1) "" else "${selectedRepeat}x",
                    color = MaterialTheme.colorScheme.onSecondary,
                    onClick = { openRepeatDialog.value = true },
                )
            }

            when {
                openCategoryDialog.value ->
                    DialogCategory(
                        onItemSelected = {
                            selectedCategory = it
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

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PrimaryButton(
                    title = "Cancelar",
                    onClick = onCancel,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                SecondaryButton(
                    title = if (isSaving) "Salvando..." else "Adicionar",
                    onClick = {
                        val monthId = currentMonthId
                        if (monthId != null && !isSaving) {
                            isSaving = true

                            val entryValue = parseBRLInputToDouble(value.text)

                            val entry =
                                Entry(
                                    uuid = Uuid.random().toString(),
                                    name = entryName.ifBlank { "Novo lançamento" },
                                    value = entryValue,
                                    date = selectedDate,
                                    repeat = selectedRepeat,
                                    type = if (selectedEntryPositive) EntryType.INCOME else EntryType.EXPENSE,
                                    category = selectedCategory ?: Category(),
                                )

                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    entryRepository.insertEntry(entry, monthId)
                                }
                                onEntrySaved()
                            }
                        }
                    },
                )
            }
        }
    }
}
