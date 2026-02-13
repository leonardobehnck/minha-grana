package com.minhagrana.models.repositories.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.Categories
import com.minhagrana.entities.Category
import com.minhagrana.models.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
) : CategoryRepository {
    override fun getAllCategoriesFlow(): Flow<List<Category>> = databaseHelper.getAllCategoriesFlow()

    override suspend fun getAllCategories(): List<Category> = databaseHelper.getAllCategories()

    override suspend fun getCategoryById(id: Int): Category? = databaseHelper.getCategoryById(id)

    override suspend fun getCategoryByName(name: String): Category? = databaseHelper.getCategoryByName(name)

    override suspend fun insertCategory(category: Category): Long = databaseHelper.insertCategory(category)

    override suspend fun insertDefaultCategoriesIfEmpty() {
        val existingCategories = databaseHelper.getAllCategories()
        if (existingCategories.isEmpty()) {
            val defaultCategories = Categories().categories
            defaultCategories.forEach { category ->
                databaseHelper.insertCategory(category)
            }
        }
    }
}
