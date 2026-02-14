package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    title: String,
    subtitle: String,
    description: String = "",
    descriptionColor: Color = MaterialTheme.colorScheme.error,
    actionButtonText: String,
    dismissButtonText: String,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit = {},
    onConfirmSelected: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Header4(
                    title = title,
                )
                if (subtitle.isNotEmpty()) {
                    Paragraph(
                        title = subtitle,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                if (description.isNotEmpty()) {
                    Paragraph(
                        color = descriptionColor,
                        title = description,
                    )
                }
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
                            title = dismissButtonText,
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
                            title = actionButtonText,
                            colorButton = MaterialTheme.colorScheme.error,
                            onClick = onConfirmSelected,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    AppTheme {
        Dialog(
            title = "Tem certeza que deseja deletar?",
            subtitle = "",
            onDismiss = {},
            showBottomSheet = true,
            actionButtonText = "Excluir",
            dismissButtonText = "Cancelar",
        )
    }
}
