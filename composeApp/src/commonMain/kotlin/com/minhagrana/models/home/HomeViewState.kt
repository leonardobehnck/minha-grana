package com.minhagrana.models.home

import com.minhagrana.entities.Year

sealed class HomeViewState {
    data object Idle : HomeViewState()

    data object Loading : HomeViewState()

    data class Success(
        val year: Year,
        val userName: String,
    ) : HomeViewState()

    data class Error(
        val message: String,
    ) : HomeViewState()

    data class NoConnection(
        val message: String,
    ) : HomeViewState()
}
