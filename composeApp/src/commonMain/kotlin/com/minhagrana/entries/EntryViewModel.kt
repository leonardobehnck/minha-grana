package com.minhagrana.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.Entry
import com.minhagrana.models.entries.EntryInteraction
import com.minhagrana.models.entries.EntryViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EntryViewModel(
    // private val entryRepository: EntryRepository,
) : ViewModel() {
    private val interactions = Channel<EntryInteraction>(Channel.UNLIMITED)
    private val states =
        MutableStateFlow<EntryViewState>(EntryViewState.Idle)

    fun bind() = states.asStateFlow()

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is EntryInteraction.OnEntryDeleted -> deleteEntry()
                    is EntryInteraction.OnEntryUpdated -> updateEntry(interaction.entry)
                    is EntryInteraction.OnEntrySelected -> fetchEntry(interaction.entry)
                }
            }
        }
    }

    private fun fetchEntry(entry: Entry) {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value =
                EntryViewState.Success(
                    Entry(
                        name = entry.name,
                        value = entry.value,
                        date = entry.date,
                    ),
                )
        }
    }

    private fun deleteEntry() {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntryViewState.Loading
        }
    }

    private fun updateEntry(entry: Entry) {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            delay(1000)
            states.value = EntryViewState.Loading
        }
    }
}
