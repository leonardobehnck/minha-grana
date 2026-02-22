package com.minhagrana.models.services

import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.models.repositories.YearRepository
import com.minhagrana.ui.monthNamePtBr
import com.minhagrana.ui.parseDateDDMMYYYY

class MonthResolver(
    private val databaseInitializer: DatabaseInitializer,
    private val yearRepository: YearRepository,
) {
    /**
     * Resolves the month ID from a date string in "dd/MM/yyyy" format.
     * Returns null if the date is invalid or the month is not found.
     */
    suspend fun resolveMonthId(dateString: String): Long? {
        val (_, monthNumber, yearNumber) = parseDateDDMMYYYY(dateString) ?: return null
        val user = databaseInitializer.initialize()
        val year = yearRepository.getYearOrCreate(user.uuid, yearNumber)
        val monthName = monthNamePtBr(monthNumber)
        return year.months
            .find { it.name == monthName }
            ?.id
            ?.toLong()
    }
}
