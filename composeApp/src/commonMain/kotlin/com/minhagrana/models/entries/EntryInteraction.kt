package com.minhagrana.models.entries

import com.minhagrana.entities.Entry

sealed class EntryInteraction {
    data class OnEntrySelected(
        val entry: Entry,
    ) : EntryInteraction()

    data class OnNewEntry(
        val monthId: Long,
    ) : EntryInteraction()

    data class OnEntryUpdated(
        val entry: Entry,
    ) : EntryInteraction()

    data object OnEntryDeleted : EntryInteraction()
}
