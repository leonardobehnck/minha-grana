package com.minhagrana.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.Year
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    // private val yearRepository: YearRepository,
) : ViewModel() {
    private val interactions = Channel<HomeInteraction>(Channel.UNLIMITED)
    private val states =
        MutableStateFlow<HomeViewState>(HomeViewState.Idle)

    fun bind() = states.asStateFlow()

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is HomeInteraction.OnScreenOpened -> fetchYear()

                    is HomeInteraction.OnNextYearSelected -> fetchNextYear()

                    is HomeInteraction.OnPreviousYearSelected -> fetchPreviousYear()
                }
            }
        }
    }

    private fun fetchYear() {
        states.value = HomeViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value =
                HomeViewState.Success(
                    Year(
                        months = emptyList(),
                    ),
                )
        }
    }

    private fun fetchNextYear() {
        states.value = HomeViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value =
                HomeViewState.Success(
                    Year(
                        months = emptyList(),
                    ),
                )
        }
    }

    private fun fetchPreviousYear() {
        states.value = HomeViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value =
                HomeViewState.Success(
                    Year(
                        months = emptyList(),
                    ),
                )
        }
    }
}
