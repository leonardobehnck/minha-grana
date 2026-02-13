package com.minhagrana.database

import com.minhagrana.entities.User
import com.minhagrana.entities.Year
import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.UserRepository
import com.minhagrana.models.repositories.YearRepository

class DatabaseInitializer(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val yearRepository: YearRepository,
) {
    private var isInitialized = false

    suspend fun initialize(): User {
        if (isInitialized) {
            return userRepository.getOrCreateDefaultUser()
        }

        // Insert default categories if empty
        categoryRepository.insertDefaultCategoriesIfEmpty()

        // Get or create default user
        val user = userRepository.getOrCreateDefaultUser()

        // Get or create current year with months
        yearRepository.getCurrentYearOrCreate(user.id.toLong())

        isInitialized = true
        return user
    }

    suspend fun getCurrentYear(userId: Long): Year = yearRepository.getCurrentYearOrCreate(userId)
}
