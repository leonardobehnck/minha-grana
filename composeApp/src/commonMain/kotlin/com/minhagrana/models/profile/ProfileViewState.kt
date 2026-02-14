package com.minhagrana.models.profile

import com.minhagrana.entities.User

sealed class ProfileViewState {
    data object Idle : ProfileViewState()

    data object Loading : ProfileViewState()

    data class Success(
        val user: User,
    ) : ProfileViewState()

    data class Error(
        val message: String,
    ) : ProfileViewState()

    data object AccountDeleted : ProfileViewState()
}
