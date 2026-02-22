package com.minhagrana.models.newentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.YearRepository
import com.minhagrana.ui.monthNamePtBr
import com.minhagrana.ui.parseBRLInputToDouble
import com.minhagrana.ui.parseDateDDMMYYYY
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class NewEntryViewModel(
    private val databaseInitializer: DatabaseInitializer,
    private val yearRepository: YearRepository,
    private val entryRepository: EntryRepository,
) : ViewModel() {
    private val interactions = Channel<NewEntryInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<NewEntryViewState>(NewEntryViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: NewEntryInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    init {
        states.value = NewEntryViewState.Idle

        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is NewEntryInteraction.OnSaveClicked -> saveEntry(interaction.form)
                    is NewEntryInteraction.OnScreenOpened -> states.value = NewEntryViewState.Success
                }
            }
        }
    }

    private fun saveEntry(form: NewEntryFormState) {
        val parsed = parseDateDDMMYYYY(form.date)
        if (parsed == null) {
            states.value = NewEntryViewState.Error("Data inválida")
            return
        }

        val (_, monthNumber, yearNumber) = parsed
        val entryValue = parseBRLInputToDouble(form.valueText)

        val entry =
            Entry(
                uuid = Uuid.random().toString(),
                name = form.name.ifBlank { "Novo lançamento" },
                value = entryValue,
                date = form.date,
                type = if (form.isIncome) EntryType.INCOME else EntryType.EXPENSE,
                category = form.category ?: com.minhagrana.entities.Category(),
            )

        states.value = NewEntryViewState.Loading

        viewModelScope.launch {
            try {
                val user = databaseInitializer.initialize()
                val year = yearRepository.getYearOrCreate(user.uuid, yearNumber)
                val monthName = monthNamePtBr(monthNumber)
                val month = year.months.find { it.name == monthName }
                val monthId = month?.id?.toLong()

                if (monthId != null) {
                    entryRepository.insertEntry(entry, monthId)
                    states.value = NewEntryViewState.EntrySaved
                } else {
                    states.value = NewEntryViewState.Error("Mês não encontrado")
                }
            } catch (e: Exception) {
                states.value = NewEntryViewState.Error(e.message ?: "Erro ao salvar lançamento")
            }
        }
    }
}
