package com.minhagrana.home

sealed class HomeInteraction {
    data object OnScreenOpened : HomeInteraction()

    data object OnNextYearSelected : HomeInteraction()

    data object OnPreviousYearSelected : HomeInteraction()
}
