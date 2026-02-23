package com.minhagrana.database

import com.minhagrana.entities.User
import com.minhagrana.entities.Year
import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.UserRepository
import com.minhagrana.models.repositories.YearRepository

class DatabaseInitializer(
    private val categoryRepository: CategoryRepository,
    private val entryRepository: EntryRepository,
    private val monthRepository: MonthRepository,
    private val userRepository: UserRepository,
    private val yearRepository: YearRepository,
) {
    private var isInitialized = false

    suspend fun initialize(isDebug: Boolean = false): User {
        if (isInitialized) {
            return userRepository.getOrCreateDefaultUser()
        }

        // Insert default categories if empty
        categoryRepository.insertDefaultCategoriesIfEmpty()

        // Get or create default user
        val user = userRepository.getOrCreateDefaultUser()

        // Get or create current year with months
        yearRepository.getCurrentYearOrCreate(user.uuid)

        if (isDebug) {
            seedDebugDataIfEnabled(user.uuid)
        }

        isInitialized = true
        return user
    }

    private suspend fun seedDebugDataIfEnabled(userUuid: String) {
        if (!DebugSeed.ENABLED) {
            return
        }

        DebugSeed.run(
            userUuid = userUuid,
            categoryRepository = categoryRepository,
            entryRepository = entryRepository,
            monthRepository = monthRepository,
            yearRepository = yearRepository,
            currentYear = com.minhagrana.util.currentYear(),
        )
    }

    suspend fun getCurrentYear(userUuid: String): Year = yearRepository.getCurrentYearOrCreate(userUuid)
}
