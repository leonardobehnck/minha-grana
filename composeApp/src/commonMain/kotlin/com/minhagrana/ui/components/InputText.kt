package com.minhagrana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme
import kotlin.math.abs

@Composable
fun InputText(
    hint: String = "",
    title: String = "",
    maxLength: Int = 25,
    warningText: String = "",
    focusRequester: FocusRequester = FocusRequester(),
    warningTextVisibility: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit = {},
    textFieldValue: TextFieldValue = TextFieldValue(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        OutlinedTextField(
            modifier =
                Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            value = textFieldValue,
            onValueChange = {
                if (it.text.length <= maxLength) {
                    onValueChange(it)
                }
            },
            textStyle = MaterialTheme.typography.labelLarge,
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            placeholder = {
                Text(
                    hint,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                ),
        )
        if (warningTextVisibility) {
            Text(
                text = warningText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

class BRLVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.filter { it.isDigit() }
        val parsedValue = if (originalText.isNotEmpty()) originalText.toLong() else 0L

        val formattedValue = formatToBRL(parsedValue)

        val cursorOffset = formattedValue.length - originalText.length

        return TransformedText(
            AnnotatedString(formattedValue),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = (offset + cursorOffset).coerceAtMost(formattedValue.length)

                override fun transformedToOriginal(offset: Int): Int = (offset - cursorOffset).coerceAtLeast(0)
            },
        )
    }

    fun formatToBRL(value: Long): String {
        val isNegative = value < 0
        val absValue = abs(value)
        val intPart = absValue / 100
        val decPart = absValue % 100

        val intStr =
            if (intPart == 0L) {
                "0"
            } else {
                intPart
                    .toString()
                    .reversed()
                    .chunked(3)
                    .joinToString(".")
                    .reversed()
            }
        val decStr = decPart.toString().padStart(2, '0')

        val sign = if (isNegative) "-" else ""
        return "${sign}R$ $intStr,$decStr"
    }
}

@Preview
@Composable
fun PreviewInputText() {
    AppTheme {
        InputText(
            title = "Nome Completo",
        )
    }
}
