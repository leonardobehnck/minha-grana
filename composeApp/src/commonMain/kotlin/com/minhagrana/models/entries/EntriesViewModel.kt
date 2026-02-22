package com.minhagrana.models.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.entities.Entry
import com.minhagrana.entities.Month
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.YearRepository
import com.minhagrana.util.currentMonthNumber
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val databaseInitializer: DatabaseInitializer,
    private val yearRepository: YearRepository,
    private val monthRepository: MonthRepository,
    private val entryRepository: EntryRepository,
) : ViewModel() {
    private val interactions = Channel<EntriesInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<EntriesViewState>(EntriesViewState.Idle)

    private var currentMonthUuid: String? = null
    private var currentMonthId: Long = -1
    private var months: List<Month> = emptyList()
    private var currentMonthIndex: Int = 0
    private var currentYearId: Long = -1

    fun bind() = states.asStateFlow()

    fun interact(interaction: EntriesInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    fun setCurrentMonth(
        monthUuid: String,
        yearId: Long,
    ) {
        currentMonthUuid = monthUuid
        currentYearId = yearId
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is EntriesInteraction.OnMonthSelected -> fetchMonth()
                    is EntriesInteraction.OnNextMonthSelected -> fetchNextMonth()
                    is EntriesInteraction.OnPreviousYearSelected -> fetchPreviousMonth()
                    is EntriesInteraction.OnEntryAdded -> addEntry(interaction.entry)
                    is EntriesInteraction.OnEntryDeleted -> deleteEntry()
                }
            }
        }
    }

    private fun fetchMonth() {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            try {
                if (currentYearId <= 0) {
                    val user = databaseInitializer.initialize()
                    val year = yearRepository.getCurrentYearOrCreate(user.uuid)
                    currentYearId = year.id.toLong()
                    months = year.months
                    if (currentMonthUuid == null && months.isNotEmpty()) {
                        val index = (currentMonthNumber() - 1).coerceIn(0, months.lastIndex)
                        currentMonthUuid = months[index].uuid
                        currentMonthIndex = index
                    }
                } else {
                    months = monthRepository.getMonthsByYearId(currentYearId)
                }

                val monthUuid = currentMonthUuid
                if (monthUuid != null) {
                    val monthEntity = monthRepository.getMonthByUuid(monthUuid)
                    if (monthEntity != null) {
                        currentMonthId = monthEntity.id.toLong()
                        currentMonthIndex = months.indexOfFirst { it.uuid == monthUuid }.takeIf { it >= 0 } ?: currentMonthIndex
                        loadMonthWithEntries(monthEntity)
                    } else {
                        states.value = EntriesViewState.Error("Mês não encontrado")
                    }
                } else if (months.isNotEmpty()) {
                    val month = months.first()
                    currentMonthId = month.id.toLong()
                    currentMonthUuid = month.uuid
                    currentMonthIndex = 0
                    val monthEntity = monthRepository.getMonthByUuid(month.uuid) ?: month
                    loadMonthWithEntries(monthEntity)
                } else {
                    states.value = EntriesViewState.Error("Nenhum mês encontrado")
                }
            } catch (e: Exception) {
                states.value = EntriesViewState.Error(e.message ?: "Erro ao carregar mês")
            }
        }
    }

    private fun fetchNextMonth() {
        if (currentMonthIndex < months.lastIndex) {
            currentMonthIndex++
            val month = months[currentMonthIndex]
            currentMonthId = month.id.toLong()
            currentMonthUuid = month.uuid
            loadMonthWithEntries(month)
        }
    }

    private fun fetchPreviousMonth() {
        if (currentMonthIndex > 0) {
            currentMonthIndex--
            val month = months[currentMonthIndex]
            currentMonthId = month.id.toLong()
            currentMonthUuid = month.uuid
            loadMonthWithEntries(month)
        }
    }

    private fun loadMonthWithEntries(month: Month) {
        viewModelScope.launch {
            try {
                val entries = entryRepository.getEntriesByMonthId(month.id.toLong())
                val monthWithEntries = month.copy(entries = entries)
                states.value = EntriesViewState.Success(monthWithEntries)
            } catch (e: Exception) {
                states.value = EntriesViewState.Error(e.message ?: "Erro ao carregar lançamentos")
            }
        }
    }

    private fun addEntry(entry: Entry) {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            try {
                if (currentMonthId > 0) {
                    entryRepository.insertEntry(entry, currentMonthId)
                    refreshCurrentMonth()
                } else {
                    states.value = EntriesViewState.Error("Mês não selecionado")
                }
            } catch (e: Exception) {
                states.value = EntriesViewState.Error(e.message ?: "Erro ao adicionar lançamento")
            }
        }
    }

    private fun deleteEntry() {
        states.value = EntriesViewState.Loading
        viewModelScope.launch {
            try {
                refreshCurrentMonth()
            } catch (e: Exception) {
                states.value = EntriesViewState.Error(e.message ?: "Erro ao deletar lançamento")
            }
        }
    }

    private suspend fun refreshCurrentMonth() {
        val monthUuid = currentMonthUuid
        if (monthUuid != null) {
            val month = monthRepository.getMonthByUuid(monthUuid)
            if (month != null) {
                loadMonthWithEntries(month)
            }
        }
    }
}
