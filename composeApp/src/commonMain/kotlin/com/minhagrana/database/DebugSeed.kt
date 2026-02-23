package com.minhagrana.database

import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.YearRepository

expect object DebugSeed {
    val ENABLED: Boolean

    suspend fun run(
        userUuid: String,
        categoryRepository: CategoryRepository,
        entryRepository: EntryRepository,
        monthRepository: MonthRepository,
        yearRepository: YearRepository,
        currentYear: Int,
    )
}
