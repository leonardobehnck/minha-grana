package com.minhagrana.models.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.models.repositories.YearRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val yearRepository: YearRepository,
    private val databaseInitializer: DatabaseInitializer,
) : ViewModel() {
    private val interactions = Channel<HomeInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<HomeViewState>(HomeViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: HomeInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is HomeInteraction.OnScreenOpened -> fetchCurrentYear()
                }
            }
        }
    }

    private fun fetchCurrentYear() {
        states.value = HomeViewState.Loading
        viewModelScope.launch {
            try {
                val user = databaseInitializer.initialize()
                val userId = user.id.toLong()

                val currentYear = yearRepository.getCurrentYearOrCreate(userId)
                val yearWithMonths = yearRepository.getYearById(currentYear.id.toLong())

                if (yearWithMonths != null) {
                    states.value = HomeViewState.Success(yearWithMonths)
                } else {
                    states.value = HomeViewState.Error("Ano não encontrado")
                }
            } catch (e: Exception) {
                states.value = HomeViewState.Error(e.message ?: "Erro ao carregar dados")
            }
        }
    }
}
