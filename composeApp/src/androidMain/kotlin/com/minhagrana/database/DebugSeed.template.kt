package com.minhagrana.database

import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.YearRepository

actual object DebugSeed {
    actual val ENABLED: Boolean = false
    actual val VERSION: Int = 0

    actual suspend fun run(
        userUuid: String,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
        yearRepository: YearRepository,
        currentYear: Int,
    ) {
    }
}
