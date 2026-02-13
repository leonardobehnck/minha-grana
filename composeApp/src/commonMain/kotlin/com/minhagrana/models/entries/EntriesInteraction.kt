package com.minhagrana.models.entries

import com.minhagrana.entities.Entry

sealed class EntriesInteraction {
    data object OnMonthSelected : EntriesInteraction()

    data class OnNextMonthSelected(
        var year: String,
    ) : EntriesInteraction()

    data class OnPreviousYearSelected(
        var year: String,
    ) : EntriesInteraction()

    data object OnEntryDeleted : EntriesInteraction()

    data class OnEntryAdded(
        val entry: Entry,
    ) : EntriesInteraction()
}
