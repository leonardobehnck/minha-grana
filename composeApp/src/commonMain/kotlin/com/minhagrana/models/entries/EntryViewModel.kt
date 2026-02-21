package com.minhagrana.models.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.services.MonthResolver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EntryViewModel(
    private val entryRepository: EntryRepository,
    private val categoryRepository: CategoryRepository,
    private val monthRepository: MonthRepository,
    private val monthResolver: MonthResolver,
) : ViewModel() {
    private val interactions = Channel<EntryInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<EntryViewState>(EntryViewState.Idle)

    private var currentEntry: Entry? = null
    private var currentMonthId: Long = -1

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    fun bind() = states.asStateFlow()

    fun interact(interaction: EntryInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }


    init {
        viewModelScope.launch {
            loadCategories()

            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is EntryInteraction.OnEntryDeleted -> deleteEntry()
                    is EntryInteraction.OnEntryUpdated -> updateEntry(interaction.entry)
                    is EntryInteraction.OnEntrySelected -> fetchEntry(interaction.entry)
                    is EntryInteraction.OnScreenOpened -> fetchEntryByUuid(interaction.entryUuid)
                }
            }
        }
    }

    private suspend fun loadCategories() {
        try {
            _categories.value = categoryRepository.getAllCategories()
        } catch (e: Exception) {
            _categories.value = emptyList()
        }
    }

    private fun fetchEntry(entry: Entry) {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            try {
                currentEntry = entry
                states.value = EntryViewState.Success(entry)
            } catch (e: Exception) {
                states.value = EntryViewState.Error(e.message ?: "Erro ao carregar lançamento")
            }
        }
    }

    private fun fetchEntryByUuid(entryUuid: String) {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            try {
                val entry = entryRepository.getEntryByUuid(entryUuid)
                if (entry != null) {
                    currentEntry = entry
                    states.value = EntryViewState.Success(entry)
                } else {
                    states.value = EntryViewState.Error("Lançamento não encontrado")
                }
            } catch (e: Exception) {
                states.value = EntryViewState.Error(e.message ?: "Erro ao carregar lançamento")
            }
        }
    }


    private fun deleteEntry() {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            try {
                val entry = currentEntry
                if (entry != null) {
                    entryRepository.deleteEntry(entry.id)
                    states.value = EntryViewState.Idle
                } else {
                    states.value = EntryViewState.Error("Lançamento não encontrado")
                }
            } catch (e: Exception) {
                states.value = EntryViewState.Error(e.message ?: "Erro ao deletar lançamento")
            }
        }
    }

    private fun updateEntry(entry: Entry) {
        states.value = EntryViewState.Loading
        viewModelScope.launch {
            try {
                when {
                    entry.id > 0 -> {
                        val previousEntry = currentEntry
                        entryRepository.updateEntry(entry)

                        val previousMonthId = previousEntry?.date?.let { monthResolver.resolveMonthId(it) }
                        val newMonthId = monthResolver.resolveMonthId(entry.date)

                        if (previousMonthId != null && newMonthId != null && previousMonthId != newMonthId) {
                            entryRepository.moveEntryToMonth(entry.id, newMonthId)
                            monthRepository.recalculateMonthTotals(previousMonthId)
                            monthRepository.recalculateMonthTotals(newMonthId)
                        }

                        currentEntry = entry
                        states.value = EntryViewState.Success(entry)
                    }

                    currentMonthId > 0 -> {
                        val newId = entryRepository.insertEntry(entry, currentMonthId)
                        val newEntry = entry.copy(id = newId.toInt())
                        currentEntry = newEntry
                        states.value = EntryViewState.Success(newEntry)
                    }

                    else -> {
                        states.value = EntryViewState.Error("Mês não selecionado")
                    }
                }
            } catch (e: Exception) {
                states.value = EntryViewState.Error(e.message ?: "Erro ao atualizar lançamento")
            }
        }
    }
}
