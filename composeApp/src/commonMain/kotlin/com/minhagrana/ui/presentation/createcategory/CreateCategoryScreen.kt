package com.minhagrana.ui.presentation.createcategory

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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.minhagrana.models.createcategory.CreateCategoryFormState
import com.minhagrana.models.createcategory.CreateCategoryInteraction
import com.minhagrana.models.createcategory.CreateCategoryViewModel
import com.minhagrana.models.createcategory.CreateCategoryViewState
import com.minhagrana.ui.components.BasicInputText
import com.minhagrana.ui.components.ColorPickerGrid
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.PrimaryButton
import com.minhagrana.ui.theme.Elevation
import com.minhagrana.ui.theme.categoryPalette
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.category_color_label
import minhagrana.composeapp.generated.resources.category_name_hint
import minhagrana.composeapp.generated.resources.create_category
import minhagrana.composeapp.generated.resources.error_category_duplicate
import minhagrana.composeapp.generated.resources.error_category_empty
import minhagrana.composeapp.generated.resources.error_category_no_color
import minhagrana.composeapp.generated.resources.save
import minhagrana.composeapp.generated.resources.something_went_wrong
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun CreateCategoryScreen(
    onCategoryCreated: () -> Unit,
    viewModel: CreateCategoryViewModel = koinInject(),
) {
    val state by viewModel.bind().collectAsState()

    LaunchedEffect(state) {
        if (state is CreateCategoryViewState.Saved) {
            onCategoryCreated()
        }
    }

    CreateCategoryContent(
        state = state,
        onSaveClicked = { form -> viewModel.interact(CreateCategoryInteraction.OnSaveClicked(form)) },
    )
}

@Composable
private fun CreateCategoryContent(
    state: CreateCategoryViewState,
    onSaveClicked: (CreateCategoryFormState) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable(
        stateSaver =
            Saver(
                save = { it?.value?.toLong() },
                restore = { Color(value = it.toULong()) },
            ),
    ) { mutableStateOf<Color?>(null) }

    val errorText: String? =
        when ((state as? CreateCategoryViewState.Error)?.type) {
            CreateCategoryViewState.Error.ErrorType.EMPTY_NAME -> stringResource(Res.string.error_category_empty)
            CreateCategoryViewState.Error.ErrorType.DUPLICATE_NAME -> stringResource(Res.string.error_category_duplicate)
            CreateCategoryViewState.Error.ErrorType.NO_COLOR -> stringResource(Res.string.error_category_no_color)
            CreateCategoryViewState.Error.ErrorType.GENERIC -> stringResource(Res.string.something_went_wrong)
            null -> null
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
            Header1(title = stringResource(Res.string.create_category))

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
                    if (errorText != null) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }

                    BasicInputText(
                        hint = stringResource(Res.string.category_name_hint),
                        value = name,
                        onValueChange = { name = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    )

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = stringResource(Res.string.category_color_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            ColorPickerGrid(
                                colors = categoryPalette,
                                selected = selectedColor,
                                onSelect = { selectedColor = it },
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            PrimaryButton(
                title = stringResource(Res.string.save),
                enabled = state !is CreateCategoryViewState.Loading,
                onClick = {
                    onSaveClicked(
                        CreateCategoryFormState(
                            name = name,
                            color = selectedColor,
                        ),
                    )
                },
            )
        }
    }
}
