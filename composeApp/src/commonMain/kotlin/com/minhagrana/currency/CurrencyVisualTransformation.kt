package com.minhagrana.currency

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation(
    private val currency: Currency,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.filter { it.isDigit() }
        val parsedValue = if (originalText.isNotEmpty()) originalText.toLongOrNull() ?: 0L else 0L

        val formatted = formatFromMinorUnits(parsedValue, currency)
        val cursorOffset = formatted.length - originalText.length

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = (offset + cursorOffset).coerceAtMost(formatted.length)

                override fun transformedToOriginal(offset: Int): Int = (offset - cursorOffset).coerceAtLeast(0)
            },
        )
    }
}
