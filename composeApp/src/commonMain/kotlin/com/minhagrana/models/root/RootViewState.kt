package com.minhagrana.models.root

sealed class RootViewState {
    data object Idle : RootViewState()

    data object Loading : RootViewState()

    data object HasUser : RootViewState()

    data object NoUser : RootViewState()
}
