package com.minhagrana.models.root

sealed class RootInteraction {
    data object CheckUserExists : RootInteraction()
}
