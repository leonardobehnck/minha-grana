package com.minhagrana.models.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.models.repositories.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RootViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val interactions = Channel<RootInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<RootViewState>(RootViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: RootInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is RootInteraction.CheckUserExists -> checkUserExists()
                }
            }
        }
    }

    private fun checkUserExists() {
        states.value = RootViewState.Loading
        viewModelScope.launch {
            val hasUser = userRepository.getAllUsersFlow().first().isNotEmpty()
            states.value = if (hasUser) RootViewState.HasUser else RootViewState.NoUser
        }
    }
}
