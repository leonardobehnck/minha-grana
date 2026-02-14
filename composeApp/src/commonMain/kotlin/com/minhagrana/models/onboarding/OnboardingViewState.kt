package com.minhagrana.models.onboarding

import com.minhagrana.entities.User

sealed class OnboardingViewState {
    data object Idle : OnboardingViewState()

    data object Loading : OnboardingViewState()

    data class Success(val user: User) : OnboardingViewState()

    data class Error(val message: String) : OnboardingViewState()
}
