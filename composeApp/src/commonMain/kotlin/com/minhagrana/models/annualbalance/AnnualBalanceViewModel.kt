package com.minhagrana.models.annualbalance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.entities.Year
import com.minhagrana.models.repositories.YearRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AnnualBalanceViewModel(
    private val yearRepository: YearRepository,
    private val databaseInitializer: DatabaseInitializer,
) : ViewModel() {
    private val interactions = Channel<AnnualBalanceInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<AnnualBalanceViewState>(AnnualBalanceViewState.Idle)

    private var currentUserId: Long = -1
    private var years: List<Year> = emptyList()
    private var currentYearIndex: Int = 0

    fun bind() = states.asStateFlow()

    fun interact(interaction: AnnualBalanceInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is AnnualBalanceInteraction.OnScreenOpened -> initializeAndFetchYear()
                    is AnnualBalanceInteraction.OnNextYearSelected -> fetchNextYear()
                    is AnnualBalanceInteraction.OnPreviousYearSelected -> fetchPreviousYear()
                }
            }
        }
    }

    private fun initializeAndFetchYear() {
        states.value = AnnualBalanceViewState.Loading
        viewModelScope.launch {
            try {
                val user = databaseInitializer.initialize()
                currentUserId = user.id.toLong()

                years = yearRepository.getAllYears(currentUserId)

                if (years.isEmpty()) {
                    val currentYear = yearRepository.getCurrentYearOrCreate(currentUserId)
                    years = listOf(currentYear)
                }

                currentYearIndex = years.lastIndex
                loadCurrentYear()
            } catch (e: Exception) {
                states.value = AnnualBalanceViewState.Error(e.message ?: "Erro ao carregar dados")
            }
        }
    }

    private fun fetchNextYear() {
        if (currentYearIndex < years.lastIndex) {
            currentYearIndex++
            loadCurrentYear()
        }
    }

    private fun fetchPreviousYear() {
        if (currentYearIndex > 0) {
            currentYearIndex--
            loadCurrentYear()
        }
    }

    private fun loadCurrentYear() {
        viewModelScope.launch {
            try {
                val year = years.getOrNull(currentYearIndex)
                if (year != null) {
                    val yearWithMonths = yearRepository.getYearById(year.id.toLong())
                    if (yearWithMonths != null) {
                        states.value = AnnualBalanceViewState.Success(yearWithMonths)
                    } else {
                        states.value = AnnualBalanceViewState.Error("Ano não encontrado")
                    }
                } else {
                    states.value = AnnualBalanceViewState.Error("Ano não encontrado")
                }
            } catch (e: Exception) {
                states.value = AnnualBalanceViewState.Error(e.message ?: "Erro ao carregar ano")
            }
        }
    }
}
