package com.minhagrana.models.annualbalance

import com.minhagrana.entities.Year

sealed class AnnualBalanceViewState {
    data object Idle : AnnualBalanceViewState()

    data object Loading : AnnualBalanceViewState()

    data class Success(
        val year: Year,
    ) : AnnualBalanceViewState()

    data class Error(
        val message: String,
    ) : AnnualBalanceViewState()

    data class NoConnection(
        val message: String,
    ) : AnnualBalanceViewState()
}
