package com.minhagrana.models.createcategory

import androidx.compose.ui.graphics.Color

data class CreateCategoryFormState(
    val name: String = "",
    val color: Color? = null,
)

sealed class CreateCategoryViewState {
    data object Idle : CreateCategoryViewState()

    data object Loading : CreateCategoryViewState()

    data object Saved : CreateCategoryViewState()

    data class Error(
        val type: ErrorType,
    ) : CreateCategoryViewState() {
        enum class ErrorType { EMPTY_NAME, DUPLICATE_NAME, NO_COLOR, GENERIC }
    }
}
