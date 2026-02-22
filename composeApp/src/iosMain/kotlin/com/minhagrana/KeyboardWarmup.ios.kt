package com.minhagrana

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
actual fun KeyboardWarmup() {
    var text by remember { mutableStateOf("") }
    var warmedUp by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    if (!warmedUp) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .size(1.dp)
                .offset(x = (-9999).dp)
                .focusRequester(focusRequester),
        )

        LaunchedEffect(Unit) {
            delay(100)
            try {
                focusRequester.requestFocus()
            } catch (_: Exception) {
            }
            delay(200)
            focusRequester.freeFocus()
            warmedUp = true
        }
    }
}
