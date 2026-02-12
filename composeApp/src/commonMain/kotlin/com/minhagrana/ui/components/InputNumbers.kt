package com.minhagrana.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputNumbers(
    textFieldValues: List<TextFieldValue>,
    onValueChange: (List<TextFieldValue>) -> Unit,
    isPassword: Boolean = false,
    onLastFieldFilled: () -> Unit = {},
) {
    val focusRequesters = remember(textFieldValues.size) { List(textFieldValues.size) { FocusRequester() } }
    val DIGIT_REGEX = Regex("\\d?")

    Row(
        modifier =
            Modifier
                .semantics { testTagsAsResourceId = true }
                .testTag("input-numbers")
                .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        textFieldValues.forEachIndexed { i, value ->
            SingleInputNumber(
                isPassword = isPassword,
                textFieldValue = value,
                onValueChange = { newValue ->
                    if (newValue.text.matches(DIGIT_REGEX)) {
                        val updatedValues = textFieldValues.update(i, newValue)
                        onValueChange(updatedValues)

                        if (i < textFieldValues.size - 1 && newValue.text.isNotEmpty()) {
                            focusRequesters[i + 1].requestFocus()
                        } else if (i == textFieldValues.size - 1 && newValue.text.isNotEmpty()) {
                            onLastFieldFilled()
                        }
                    }
                },
                onBackspacePressed = {
                    if (i > 0 && value.text.isEmpty()) {
                        focusRequesters[i - 1].requestFocus()
                    }
                },
                onBackspaceDelete = {
                    val updatedValues = textFieldValues.update(i, TextFieldValue(""))
                    onValueChange(updatedValues)
                    if (i > 0) focusRequesters[i - 1].requestFocus()
                },
                focusRequester = focusRequesters[i],
            )
        }
    }

    LaunchedEffect(focusRequesters) {
        focusRequesters.first().requestFocus()
    }
}

@Composable
fun SingleInputNumber(
    textFieldValue: TextFieldValue = TextFieldValue(),
    onValueChange: (TextFieldValue) -> Unit = {},
    onBackspacePressed: () -> Unit = {},
    onBackspaceDelete: () -> Unit = {},
    focusRequester: FocusRequester,
    isPassword: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    BasicTextField(
        modifier =
            Modifier
                .padding(5.dp)
                .focusRequester(focusRequester)
                .width(50.dp)
                .height(50.dp)
                .onKeyEvent { event ->
                    handleKeyEvent(event, textFieldValue, onBackspacePressed, onBackspaceDelete)
                },
        value = textFieldValue,
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        onValueChange = { newValue ->
            onValueChange(newValue)
            if (newValue.text.isNotEmpty()) keyboardController?.hide()
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField ->
            CustomDecorationBox(
                value = textFieldValue.text,
                innerTextField = innerTextField,
                interactionSource = interactionSource,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    interactionSource: MutableInteractionSource,
) {
    OutlinedTextFieldDefaults.DecorationBox(
        value = value,
        innerTextField = innerTextField,
        singleLine = true,
        enabled = true,
        visualTransformation = VisualTransformation.None,
        colors = colors(),
        interactionSource = interactionSource,
        container = {
            OutlinedTextFieldDefaults.Container(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors(),
                shape = OutlinedTextFieldDefaults.shape,
                focusedBorderThickness = FocusedBorderThickness,
                unfocusedBorderThickness = UnfocusedBorderThickness,
            )
        },
    )
}

private fun handleKeyEvent(
    event: KeyEvent,
    textFieldValue: TextFieldValue,
    onBackspacePressed: () -> Unit,
    onBackspaceDelete: () -> Unit,
): Boolean =
    if (event.key == Key.Backspace) {
        if (textFieldValue.text.isEmpty()) {
            onBackspacePressed()
        } else {
            onBackspaceDelete()
        }
        true
    } else {
        false
    }

private fun List<TextFieldValue>.update(
    index: Int,
    newValue: TextFieldValue,
): List<TextFieldValue> =
    toMutableList().apply {
        this[index] = newValue
    }

@Preview
@Composable
fun PreviewInputNumbers() {
    AppTheme {
        InputNumbers(
            textFieldValues =
                listOf(
                    TextFieldValue("1"),
                    TextFieldValue("2"),
                    TextFieldValue("3"),
                    TextFieldValue("4"),
                ),
            onValueChange = {},
        )
    }
}
