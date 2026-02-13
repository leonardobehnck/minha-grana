package com.minhagrana.home

sealed class HomeInteraction {
    data object OnScreenOpened : HomeInteraction()
}
