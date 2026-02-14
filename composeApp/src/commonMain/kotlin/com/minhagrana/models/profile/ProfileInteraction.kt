package com.minhagrana.models.profile

sealed class ProfileInteraction {
    data object OnScreenOpened : ProfileInteraction()

    data class OnUpdateName(
        val name: String,
    ) : ProfileInteraction()

    data object OnDeleteAccount : ProfileInteraction()
}
