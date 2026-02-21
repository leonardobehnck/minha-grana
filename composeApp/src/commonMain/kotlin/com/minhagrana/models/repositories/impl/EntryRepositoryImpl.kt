package com.minhagrana.models.repositories.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import kotlinx.coroutines.flow.Flow

class EntryRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
    private val monthRepository: MonthRepository,
) : EntryRepository {
    override fun getEntriesByMonthIdFlow(monthId: Long): Flow<List<Entry>> = databaseHelper.getEntriesByMonthIdFlow(monthId)

    override suspend fun getEntriesByMonthId(monthId: Long): List<Entry> {
        val entryEntities = databaseHelper.getEntriesByMonthId(monthId)
        return entryEntities.map { entity ->
            val category = databaseHelper.getCategoryById(entity.category_id.toInt()) ?: Category()
            Entry(
                id = entity.id.toInt(),
                uuid = entity.uuid,
                name = entity.name,
                value = entity.value_,
                date = entity.date,
                repeat = entity.repeat.toInt(),
                type = EntryType.valueOf(entity.type),
                category = category,
            )
        }
    }

    override suspend fun getEntryByUuid(uuid: String): Entry? = databaseHelper.getEntryByUuid(uuid)

    override suspend fun insertEntry(
        entry: Entry,
        monthId: Long,
    ): Long {
        val entryId = databaseHelper.insertEntry(entry, monthId)
        monthRepository.recalculateMonthTotals(monthId)
        return entryId
    }

    override suspend fun updateEntry(entry: Entry) {
        databaseHelper.updateEntry(entry)
    }

    override suspend fun moveEntryToMonth(
        entryId: Int,
        monthId: Long,
    ) {
        databaseHelper.updateEntryMonthId(entryId, monthId)
        monthRepository.recalculateMonthTotals(monthId)
    }

    override suspend fun deleteEntry(id: Int) {
        databaseHelper.deleteEntry(id)
    }
}
