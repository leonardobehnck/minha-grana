package com.minhagrana.models.home

sealed class HomeInteraction {
    data object OnScreenOpened : HomeInteraction()
}
