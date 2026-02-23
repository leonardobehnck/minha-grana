package com.minhagrana.database

import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.YearRepository
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object DebugSeedTemplate {
    const val ENABLED: Boolean = true

    suspend fun run(
        userUuid: String,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
        yearRepository: YearRepository,
        currentYear: Int,
    ) {
        val previousYear = yearRepository.getYearOrCreate(userUuid = userUuid, yearNumber = currentYear - 1)
        val currentYearEntity = yearRepository.getYearOrCreate(userUuid = userUuid, yearNumber = currentYear)

        seedYearEntriesIfEmpty(
            yearId = previousYear.id.toLong(),
            yearNumber = currentYear - 1,
            categoryRepository = categoryRepository,
            entryRepository = entryRepository,
            monthRepository = monthRepository,
        )
        seedYearEntriesIfEmpty(
            yearId = currentYearEntity.id.toLong(),
            yearNumber = currentYear,
            categoryRepository = categoryRepository,
            entryRepository = entryRepository,
            monthRepository = monthRepository,
        )
    }

    private suspend fun seedYearEntriesIfEmpty(
        yearId: Long,
        yearNumber: Int,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
    ) {
        val months = monthRepository.getMonthsByYearId(yearId)
        months.forEachIndexed { index, month ->
            val monthId = month.id.toLong()
            val existingEntries = entryRepository.getEntriesByMonthId(monthId)
            if (existingEntries.isNotEmpty()) {
                return@forEachIndexed
            }

            val monthNumber = index + 1
            seedMonthEntries(
                monthId = monthId,
                monthNumber = monthNumber,
                yearNumber = yearNumber,
                categoryRepository = categoryRepository,
                entryRepository = entryRepository,
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun seedMonthEntries(
        monthId: Long,
        monthNumber: Int,
        yearNumber: Int,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
    ) {
        val salaryCategory = categoryRepository.getCategoryByName("Salário")
        val generalCategory = categoryRepository.getCategoryByName("Geral")
        val petCategory = categoryRepository.getCategoryByName("Pet")
        val healthCategory = categoryRepository.getCategoryByName("Saúde")
        val transportCategory = categoryRepository.getCategoryByName("Transporte")

        val date = "15/${monthNumber.toString().padStart(2, '0')}/$yearNumber"

        val shouldBeNegativeMonth = monthNumber == 2 || monthNumber == 7 || monthNumber == 11

        val salaryValue = (3500 + monthNumber * 120)
        if (salaryCategory != null) {
            entryRepository.insertEntry(
                entry =
                    Entry(
                        uuid = Uuid.random().toString(),
                        name = "Salário",
                        value = salaryValue.toDouble(),
                        date = date,
                        type = EntryType.INCOME,
                        category = salaryCategory,
                    ),
                monthId = monthId,
            )
        }

        val generalMultiplier = if (shouldBeNegativeMonth) 2.3 else 1.0
        val generalExpense = ((600 + monthNumber * 35) * generalMultiplier).roundToInt().toDouble()
        if (generalCategory != null) {
            entryRepository.insertEntry(
                entry =
                    Entry(
                        uuid = Uuid.random().toString(),
                        name = "Geral",
                        value = generalExpense,
                        date = date,
                        type = EntryType.EXPENSE,
                        category = generalCategory,
                    ),
                monthId = monthId,
            )
        }

        val petExpense = ((120 + (monthNumber % 3) * 45) * 1.0).roundToInt().toDouble()
        if (petCategory != null) {
            entryRepository.insertEntry(
                entry =
                    Entry(
                        uuid = Uuid.random().toString(),
                        name = "Pet",
                        value = petExpense,
                        date = date,
                        type = EntryType.EXPENSE,
                        category = petCategory,
                    ),
                monthId = monthId,
            )
        }

        val healthMultiplier = if (shouldBeNegativeMonth) 1.8 else 1.0
        val healthExpense = ((180 + (monthNumber % 4) * 60) * healthMultiplier).roundToInt().toDouble()
        if (healthCategory != null) {
            entryRepository.insertEntry(
                entry =
                    Entry(
                        uuid = Uuid.random().toString(),
                        name = "Saúde",
                        value = healthExpense,
                        date = date,
                        type = EntryType.EXPENSE,
                        category = healthCategory,
                    ),
                monthId = monthId,
            )
        }

        val transportBase = 140 + (monthNumber % 5) * 25
        val transportTrips = if (shouldBeNegativeMonth) 5 else 3
        if (transportCategory != null) {
            (1..transportTrips).forEach { tripIndex ->
                val tripValue = ((transportBase + tripIndex * 12) * 1.0).roundToInt().toDouble()
                entryRepository.insertEntry(
                    entry =
                        Entry(
                            uuid = Uuid.random().toString(),
                            name = "Transporte",
                            value = tripValue,
                            date = date,
                            type = EntryType.EXPENSE,
                            category = transportCategory,
                        ),
                    monthId = monthId,
                )
            }
        }
    }
}

actual object DebugSeed {
    actual val ENABLED: Boolean = DebugSeedTemplate.ENABLED

    actual suspend fun run(
        userUuid: String,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
        yearRepository: YearRepository,
        currentYear: Int,
    ) {
        DebugSeedTemplate.run(
            userUuid = userUuid,
            categoryRepository = categoryRepository,
            entryRepository = entryRepository,
            monthRepository = monthRepository,
            yearRepository = yearRepository,
            currentYear = currentYear,
        )
    }
}
