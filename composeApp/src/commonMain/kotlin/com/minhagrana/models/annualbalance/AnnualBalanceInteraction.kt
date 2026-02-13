package com.minhagrana.models.annualbalance

sealed class AnnualBalanceInteraction {
    data object OnScreenOpened : AnnualBalanceInteraction()

    data object OnNextYearSelected : AnnualBalanceInteraction()

    data object OnPreviousYearSelected : AnnualBalanceInteraction()
}
