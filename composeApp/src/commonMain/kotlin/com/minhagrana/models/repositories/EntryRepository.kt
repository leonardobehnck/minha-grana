package com.minhagrana.models.repositories

import com.minhagrana.entities.Entry
import kotlinx.coroutines.flow.Flow

interface EntryRepository {
    fun getEntriesByMonthIdFlow(monthId: Long): Flow<List<Entry>>

    suspend fun getEntriesByMonthId(monthId: Long): List<Entry>

    suspend fun getEntryByUuid(uuid: String): Entry?

    suspend fun insertEntry(
        entry: Entry,
        monthId: Long,
    ): Long

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(id: Int)
}
