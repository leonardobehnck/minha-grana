package com.minhagrana.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasicInputText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    maxLength: Int = 25,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(value))
    }

    Box(
        modifier = modifier,
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                color = textColor,
                style = textStyle,
            )
        }
        BasicTextField(
            modifier =
                Modifier
                    .clickable {
                        textFieldValue =
                            textFieldValue.copy(
                                selection = TextRange(textFieldValue.text.length),
                            )
                    }.fillMaxWidth(),
            value = textFieldValue,
            maxLines = 1,
            onValueChange = {
                if (it.text.length <= maxLength) {
                    textFieldValue =
                        it.copy(
                            selection = TextRange(it.text.length),
                        )
                    onValueChange(it.text)
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            textStyle =
                textStyle.copy(
                    color = textColor,
                ),
        )
    }
}

@Preview
@Composable
fun PreviewBasicInputText() {
    var text by remember { mutableStateOf("") }
    BasicInputText(
        value = text,
        onValueChange = { text = it },
        hint = "Digite algo",
    )
}
