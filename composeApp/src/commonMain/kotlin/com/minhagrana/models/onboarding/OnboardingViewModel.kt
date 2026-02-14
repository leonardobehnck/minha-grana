package com.minhagrana.models.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.User
import com.minhagrana.models.repositories.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OnboardingViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val interactions = Channel<OnboardingInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<OnboardingViewState>(OnboardingViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: OnboardingInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is OnboardingInteraction.OnCreateUser -> createUser(interaction.name)
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun createUser(name: String) {
        states.value = OnboardingViewState.Loading
        viewModelScope.launch {
            try {
                val user =
                    User(
                        uuid = Uuid.random().toString(),
                        name = name.trim().ifEmpty { "Usuário" },
                        email = "",
                        password = "",
                        balanceVisibility = true,
                    )
                val id = userRepository.insertUser(user)
                val createdUser = user.copy(id = id.toInt())
                states.value = OnboardingViewState.Success(createdUser)
            } catch (e: Exception) {
                states.value = OnboardingViewState.Error(e.message ?: "Erro ao criar usuário")
            }
        }
    }
}
