package com.minhagrana.repository

import com.minhagrana.entities.Month
import kotlinx.coroutines.flow.Flow

interface MonthRepository {
    fun getMonthsByYearIdFlow(yearId: Long): Flow<List<Month>>

    suspend fun getMonthsByYearId(yearId: Long): List<Month>

    suspend fun getMonthByUuid(uuid: String): Month?

    suspend fun getMonthById(id: Long): Month?

    suspend fun insertMonth(
        month: Month,
        yearId: Long,
    ): Long

    suspend fun updateMonthTotals(
        monthId: Long,
        income: Double,
        expense: Double,
        balance: Double,
    )

    suspend fun recalculateMonthTotals(monthId: Long)

    suspend fun deleteMonth(id: Int)
}
