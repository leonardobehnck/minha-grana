package com.minhagrana.models.newentry

import com.minhagrana.entities.Category

data class NewEntryFormState(
    val name: String = "",
    val date: String,
    val category: Category? = null,
    val isIncome: Boolean = false,
    val valueText: String = "",
)

sealed class NewEntryViewState {
    data object Idle : NewEntryViewState()

    data object Loading : NewEntryViewState()

    data object EntrySaved : NewEntryViewState()

    data object Success : NewEntryViewState()

    data class Error(
        val message: String,
    ) : NewEntryViewState()
}
