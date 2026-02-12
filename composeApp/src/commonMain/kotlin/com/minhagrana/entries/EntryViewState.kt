package com.minhagrana.models.entries

import com.minhagrana.entities.Entry

sealed class EntryViewState {
    data object Idle : EntryViewState()

    data object Loading : EntryViewState()

    data class Success(
        val entry: Entry,
    ) : EntryViewState()

    data class Error(
        val message: String,
    ) : EntryViewState()

    data class NoConnection(
        val message: String,
    ) : EntryViewState()
}
