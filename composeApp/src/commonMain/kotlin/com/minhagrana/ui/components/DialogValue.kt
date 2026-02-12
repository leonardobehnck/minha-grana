package com.minhagrana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun DialogValue(
    hint: String = "R$ 0,00",
    onValueChange: (TextFieldValue) -> Unit = {},
    textFieldValue: TextFieldValue = TextFieldValue(),
    onDismissRequest: () -> Unit,
    onSaveChanges: () -> Unit,
) {
    val focusRequester = FocusRequester()

    androidx.compose.ui.window.Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
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
            Header1(
                title = "Editar valor",
            )
            InputText(
                hint = hint,
                title = "Valor",
                focusRequester = focusRequester,
                textFieldValue = textFieldValue,
                onValueChange = { onValueChange(it) },
                visualTransformation = BRLVisualTransformation(),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
            )
            PrimaryButton(
                title = "Salvar",
                onClick = onSaveChanges,
            )
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
