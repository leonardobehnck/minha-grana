package com.minhagrana.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.Entry
import com.minhagrana.entities.Month
import com.minhagrana.models.entries.EntriesViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EntriesViewModel(
    //private val yearRepository: YearRepository,
    //private val entryRepository: EntryRepository,
) : ViewModel() {
    private val interactions = Channel<EntriesInteraction>(Channel.UNLIMITED)
    private val states =
        MutableStateFlow<EntriesViewState>(EntriesViewState.Idle)

    fun bind() = states.asStateFlow()

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is EntriesInteraction.OnMonthSelected -> fetchMonth()
                    is EntriesInteraction.OnNextMonthSelected -> fetchNextMonth(interaction.year)
                    is EntriesInteraction.OnPreviousYearSelected -> fetchPreviousMonth(interaction.year)
                    is EntriesInteraction.OnEntryAdded -> addEntry(interaction.entry)
                    is EntriesInteraction.OnEntryDeleted -> deleteEntry()
                }
            }
        }
    }

    private fun fetchMonth() {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value =
                EntriesViewState.Success(
                    Month(
                        entries = emptyList(),
                    ),
                )
        }
    }

    private fun fetchNextMonth(year: String) {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntriesViewState.Loading
        }
    }

    private fun fetchPreviousMonth(year: String) {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntriesViewState.Loading
        }
    }

    private fun addEntry(entry: Entry) {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntriesViewState.Loading
            Entry(
                name = entry.name,
                value = entry.value,
                date = entry.date,
            )
            fetchMonth()
        }
    }

    private fun deleteEntry() {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntriesViewState.Loading
            fetchMonth()
        }
    }
}
