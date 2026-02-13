package com.minhagrana.repository

import com.minhagrana.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategoriesFlow(): Flow<List<Category>>

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoryById(id: Int): Category?

    suspend fun getCategoryByName(name: String): Category?

    suspend fun insertCategory(category: Category): Long

    suspend fun insertDefaultCategoriesIfEmpty()
}
