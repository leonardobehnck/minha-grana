package com.minhagrana.database

import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.YearRepository

object DebugSeedTemplate {
    const val ENABLED: Boolean = true
    const val VERSION: Int = 0

    suspend fun run(
        userUuid: String,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
        yearRepository: YearRepository,
        currentYear: Int,
    ) {
    }
}

actual object DebugSeed {
    actual val ENABLED: Boolean = DebugSeedTemplate.ENABLED
    actual val VERSION: Int = DebugSeedTemplate.VERSION

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
