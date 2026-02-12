package com.minhagrana.models.entries

import com.minhagrana.entities.Month

sealed class EntriesViewState {
    data object Idle : EntriesViewState()

    data object Loading : EntriesViewState()

    data class Success(
        val month: Month,
    ) : EntriesViewState()

    data class Error(
        val message: String,
    ) : EntriesViewState()

    data class NoConnection(
        val message: String,
    ) : EntriesViewState()
}
