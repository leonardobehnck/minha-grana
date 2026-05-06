package com.minhagrana.models.createcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhagrana.entities.Category
import com.minhagrana.models.repositories.CategoryRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val interactions = Channel<CreateCategoryInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<CreateCategoryViewState>(CreateCategoryViewState.Idle)

    fun bind() = states.asStateFlow()

    fun interact(interaction: CreateCategoryInteraction) {
        viewModelScope.launch { interactions.send(interaction) }
    }

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    is CreateCategoryInteraction.OnScreenOpened -> states.value = CreateCategoryViewState.Idle
                    is CreateCategoryInteraction.OnSaveClicked -> save(interaction.form)
                }
            }
        }
    }

    private fun save(form: CreateCategoryFormState) {
        val trimmed = form.name.trim()
        if (trimmed.isEmpty()) {
            states.value = CreateCategoryViewState.Error(CreateCategoryViewState.Error.ErrorType.EMPTY_NAME)
            return
        }
        val color = form.color
        if (color == null) {
            states.value = CreateCategoryViewState.Error(CreateCategoryViewState.Error.ErrorType.NO_COLOR)
            return
        }

        states.value = CreateCategoryViewState.Loading

        viewModelScope.launch {
            try {
                val existing = categoryRepository.getCategoryByName(trimmed)
                if (existing != null) {
                    states.value = CreateCategoryViewState.Error(CreateCategoryViewState.Error.ErrorType.DUPLICATE_NAME)
                    return@launch
                }
                categoryRepository.insertCategory(
                    Category(name = trimmed, color = color, stringKey = null),
                )
                states.value = CreateCategoryViewState.Saved
            } catch (_: Exception) {
                states.value = CreateCategoryViewState.Error(CreateCategoryViewState.Error.ErrorType.GENERIC)
            }
        }
    }
}
