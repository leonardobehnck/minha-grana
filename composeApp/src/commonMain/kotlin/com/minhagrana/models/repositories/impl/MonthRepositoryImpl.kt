package com.minhagrana.models.repositories.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.Month
import com.minhagrana.models.repositories.MonthRepository
import kotlinx.coroutines.flow.Flow

class MonthRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
) : MonthRepository {
    override fun getMonthsByYearIdFlow(yearId: Long): Flow<List<Month>> = databaseHelper.getMonthsByYearIdFlow(yearId)

    override suspend fun getMonthsByYearId(yearId: Long): List<Month> {
        val monthEntities = databaseHelper.getMonthsByYearId(yearId)
        return monthEntities.map { entity ->
            Month(
                id = entity.id.toInt(),
                uuid = entity.uuid,
                name = entity.name,
                income = entity.income,
                expense = entity.expense,
                balance = entity.balance,
                entries = emptyList(),
            )
        }
    }

    override suspend fun getMonthByUuid(uuid: String): Month? {
        val entity = databaseHelper.getMonthByUuid(uuid) ?: return null
        return Month(
            id = entity.id.toInt(),
            uuid = entity.uuid,
            name = entity.name,
            income = entity.income,
            expense = entity.expense,
            balance = entity.balance,
            entries = emptyList(),
        )
    }

    override suspend fun getMonthById(id: Long): Month? {
        val entity = databaseHelper.getMonthsByYearId(1).find { it.id == id } ?: return null
        return Month(
            id = entity.id.toInt(),
            uuid = entity.uuid,
            name = entity.name,
            income = entity.income,
            expense = entity.expense,
            balance = entity.balance,
            entries = emptyList(),
        )
    }

    override suspend fun insertMonth(
        month: Month,
        yearId: Long,
    ): Long = databaseHelper.insertMonth(month, yearId)

    override suspend fun updateMonthTotals(
        monthId: Long,
        income: Double,
        expense: Double,
        balance: Double,
    ) = databaseHelper.updateMonthTotals(monthId, income, expense, balance)

    override suspend fun recalculateMonthTotals(monthId: Long) {
        val (income, expense, balance) = databaseHelper.calculateMonthTotals(monthId)
        databaseHelper.updateMonthTotals(monthId, income, expense, balance)
    }

    override suspend fun deleteMonth(id: Int) = databaseHelper.deleteMonth(id)
}
