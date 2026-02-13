package com.minhagrana.repository

import com.minhagrana.entities.Year
import kotlinx.coroutines.flow.Flow

interface YearRepository {
    fun getYearsByUserIdFlow(userId: Long): Flow<List<Year>>

    suspend fun getAllYears(userId: Long): List<Year>

    suspend fun getYearByUuid(uuid: String): Year?

    suspend fun getYearById(id: Long): Year?

    suspend fun getCurrentYearOrCreate(userId: Long): Year

    suspend fun insertYear(
        year: Year,
        userId: Long,
    ): Long

    suspend fun deleteYear(id: Int)
}
