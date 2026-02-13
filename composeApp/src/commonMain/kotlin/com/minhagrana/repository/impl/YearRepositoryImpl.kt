package com.minhagrana.repository.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.Month
import com.minhagrana.entities.Year
import com.minhagrana.repository.EntryRepository
import com.minhagrana.repository.MonthRepository
import com.minhagrana.repository.YearRepository
import com.minhagrana.util.currentYear
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class YearRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
    private val monthRepository: MonthRepository,
    private val entryRepository: EntryRepository,
) : YearRepository {
    companion object {
        private val MONTH_NAMES =
            listOf(
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro",
            )
    }

    override fun getYearsByUserIdFlow(userId: Long): Flow<List<Year>> = databaseHelper.getYearsByUserIdFlow(userId)

    override suspend fun getAllYears(userId: Long): List<Year> {
        val years = databaseHelper.getYearsByUserIdFlow(userId).first()
        return years.map { year ->
            val months = monthRepository.getMonthsByYearId(year.id.toLong())
            year.copy(months = months)
        }
    }

    override suspend fun getYearByUuid(uuid: String): Year? {
        val yearEntity = databaseHelper.getYearByUuid(uuid) ?: return null
        val months = monthRepository.getMonthsByYearId(yearEntity.id)
        return Year(
            id = yearEntity.id.toInt(),
            uuid = yearEntity.uuid,
            name = yearEntity.name,
            months = months,
        )
    }

    override suspend fun getYearById(id: Long): Year? {
        val yearEntity = databaseHelper.getYearById(id) ?: return null
        val months = monthRepository.getMonthsByYearId(id)
        val monthsWithEntries = months.map { m ->
            m.copy(entries = entryRepository.getEntriesByMonthId(m.id.toLong()))
        }
        return Year(
            id = yearEntity.id.toInt(),
            uuid = yearEntity.uuid,
            name = yearEntity.name,
            months = monthsWithEntries,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getCurrentYearOrCreate(userId: Long): Year {
        val currentYear = currentYear().toString()

        val existingYears = databaseHelper.getYearsByUserIdFlow(userId).first()
        val existingYear = existingYears.find { it.name == currentYear }

        if (existingYear != null) {
            var months = monthRepository.getMonthsByYearId(existingYear.id.toLong())

            // If year exists but has no months, create them
            if (months.isEmpty()) {
                months = createMonthsForYear(existingYear.id.toLong())
            }

            val monthsWithEntries = months.map { m ->
                m.copy(entries = entryRepository.getEntriesByMonthId(m.id.toLong()))
            }
            return existingYear.copy(months = monthsWithEntries)
        }

        val newYear =
            Year(
                uuid = Uuid.random().toString(),
                name = currentYear,
                months = emptyList(),
            )
        val yearId = databaseHelper.insertYear(newYear, userId)

        val months = createMonthsForYear(yearId)

        return newYear.copy(
            id = yearId.toInt(),
            months = months,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun createMonthsForYear(yearId: Long): List<Month> {
        val months = mutableListOf<Month>()
        MONTH_NAMES.forEach { monthName ->
            val month =
                Month(
                    uuid = Uuid.random().toString(),
                    name = monthName,
                    income = 0.0,
                    expense = 0.0,
                    balance = 0.0,
                    entries = emptyList(),
                )
            val monthId = monthRepository.insertMonth(month, yearId)
            months.add(month.copy(id = monthId.toInt()))
        }
        return months
    }

    override suspend fun insertYear(
        year: Year,
        userId: Long,
    ): Long = databaseHelper.insertYear(year, userId)

    override suspend fun deleteYear(id: Int) = databaseHelper.deleteYear(id)
}
