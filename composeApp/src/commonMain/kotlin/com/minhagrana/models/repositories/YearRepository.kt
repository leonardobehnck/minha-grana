package com.minhagrana.models.repositories

import com.minhagrana.entities.Year
import kotlinx.coroutines.flow.Flow

interface YearRepository {
    fun getYearsByUserUuidFlow(userUuid: String): Flow<List<Year>>

    suspend fun getAllYears(userUuid: String): List<Year>

    suspend fun getYearByUuid(uuid: String): Year?

    suspend fun getYearById(id: Long): Year?

    suspend fun getCurrentYearOrCreate(userUuid: String): Year

    suspend fun getYearOrCreate(userUuid: String, yearNumber: Int): Year

    suspend fun insertYear(
        year: Year,
        userUuid: String,
    ): Long

    suspend fun deleteYear(id: Int)
}
