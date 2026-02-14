package com.minhagrana.models.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.models.repositories.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val interactions = Channel<ProfileInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<ProfileViewState>(ProfileViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: ProfileInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is ProfileInteraction.OnScreenOpened -> loadUser()
                    is ProfileInteraction.OnUpdateName -> updateUserName(interaction.name)
                    is ProfileInteraction.OnDeleteAccount -> deleteAllData()
                }
            }
        }
    }

    private fun loadUser() {
        states.value = ProfileViewState.Loading
        viewModelScope.launch {
            try {
                val users = userRepository.getAllUsersFlow().first()
                val user = users.firstOrNull()
                if (user != null) {
                    states.value = ProfileViewState.Success(user)
                } else {
                    states.value = ProfileViewState.Error("Usuário não encontrado")
                }
            } catch (e: Exception) {
                states.value = ProfileViewState.Error(e.message ?: "Erro ao carregar usuário")
            }
        }
    }

    private fun updateUserName(name: String) {
        val currentState = states.value
        if (currentState !is ProfileViewState.Success) return
        viewModelScope.launch {
            try {
                val updatedUser = currentState.user.copy(name = name.trim().ifEmpty { currentState.user.name })
                userRepository.updateUser(updatedUser)
                states.value = ProfileViewState.Success(updatedUser)
            } catch (e: Exception) {
                states.value = ProfileViewState.Error(e.message ?: "Erro ao atualizar nome")
            }
        }
    }

    private fun deleteAllData() {
        states.value = ProfileViewState.Loading
        viewModelScope.launch {
            try {
                userRepository.deleteAllData()
                states.value = ProfileViewState.AccountDeleted
            } catch (e: Exception) {
                states.value = ProfileViewState.Error(e.message ?: "Erro ao excluir conta")
            }
        }
    }
}
