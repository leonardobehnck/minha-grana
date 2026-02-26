package com.minhagrana.database

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

    suspend fun initialize() {
        if (isInitialized) {
            return
        }

        // Insert default categories if empty
        categoryRepository.insertDefaultCategoriesIfEmpty()

        isInitialized = true
    }

    suspend fun getUserOrNull() = userRepository.getFirstUserOrNull()

    suspend fun getUser() = userRepository.getOrCreateDefaultUser()

    suspend fun onUserCreated(userUuid: String) {
        initialize()
        yearRepository.getCurrentYearOrCreate(userUuid)
    }

    suspend fun getCurrentYear(userUuid: String): Year = yearRepository.getCurrentYearOrCreate(userUuid)
}
