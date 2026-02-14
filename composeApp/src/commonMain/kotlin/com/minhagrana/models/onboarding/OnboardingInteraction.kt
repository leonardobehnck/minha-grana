package com.minhagrana.models.onboarding

sealed class OnboardingInteraction {
    data class OnCreateUser(
        val name: String,
    ) : OnboardingInteraction()
}
