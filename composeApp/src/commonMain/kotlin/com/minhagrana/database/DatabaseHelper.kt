package com.minhagrana.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.entities.User
import com.minhagrana.entities.Year
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseHelper(
    private val database: MinhaGranaDatabase,
) {
    private val queries = database.minhaGranaDatabaseQueries

    // ==================== USER ====================

    suspend fun insertUser(user: User): Long =
        database.transactionWithResult {
            queries.insertUser(
                uuid = user.uuid,
                name = user.name,
                currency_code = user.currencyCode,
            )
            queries.lastInsertRowId().executeAsOne()
        }

    fun getAllUsersFlow(): Flow<List<User>> =
        queries
            .selectAllUsers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toUser() } }

    suspend fun getAllUsers(): List<User> = queries.selectAllUsers().executeAsList().map { it.toUser() }

    suspend fun getUserByUuid(uuid: String): User? = queries.selectUserByUuid(uuid).executeAsOneOrNull()?.toUser()

    suspend fun updateUser(user: User) {
        queries.updateUser(
            name = user.name,
            currency_code = user.currencyCode,
            uuid = user.uuid,
        )
    }

    suspend fun updateUserCurrency(
        userUuid: String,
        currencyCode: String,
    ) {
        queries.updateUserCurrency(
            currency_code = currencyCode,
            uuid = userUuid,
        )
    }

    suspend fun deleteUser(id: Int) {
        queries.deleteUserById(id.toLong())
    }

    suspend fun deleteAllData() {
        queries.deleteAllEntries()
        queries.deleteAllMonths()
        queries.deleteAllYears()
        queries.deleteAllCategories()
        queries.deleteAllUsers()
    }

    // ==================== CATEGORY ====================

    suspend fun insertCategory(category: Category): Long =
        database.transactionWithResult {
            queries.insertCategory(
                name = category.name,
                color_hex = colorToHex(category.color),
                string_key = category.stringKey,
            )
            queries.lastInsertRowId().executeAsOne()
        }

    fun getAllCategoriesFlow(): Flow<List<Category>> =
        queries
            .selectAllCategories()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toCategory() } }

    suspend fun getAllCategories(): List<Category> = queries.selectAllCategories().executeAsList().map { it.toCategory() }

    suspend fun getCategoryById(id: Int): Category? = queries.selectCategoryById(id.toLong()).executeAsOneOrNull()?.toCategory()

    suspend fun getCategoryByName(name: String): Category? = queries.selectCategoryByName(name).executeAsOneOrNull()?.toCategory()

    suspend fun deleteCategory(id: Int) {
        queries.deleteCategoryById(id.toLong())
    }

    // ==================== YEAR ====================

    suspend fun insertYear(
        year: Year,
        userUuid: String,
    ): Long =
        database.transactionWithResult {
            queries.insertYear(
                uuid = year.uuid,
                name = year.name,
                user_uuid = userUuid,
            )
            queries.lastInsertRowId().executeAsOne()
        }

    fun getYearsByUserUuidFlow(userUuid: String): Flow<List<Year>> =
        queries
            .selectYearsByUserUuid(userUuid)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toYear(emptyList()) } }

    suspend fun getYearById(id: Long): YearEntity? = queries.selectYearById(id).executeAsOneOrNull()

    suspend fun getYearByUuid(uuid: String): YearEntity? = queries.selectYearByUuid(uuid).executeAsOneOrNull()

    suspend fun deleteYear(id: Int) {
        queries.deleteYearById(id.toLong())
    }

    // ==================== MONTH ====================

    suspend fun insertMonth(
        month: Month,
        yearId: Long,
    ): Long =
        database.transactionWithResult {
            queries.insertMonth(
                uuid = month.uuid,
                name = month.name,
                income = month.income,
                expense = month.expense,
                balance = month.balance,
                year_id = yearId,
            )
            queries.lastInsertRowId().executeAsOne()
        }

    fun getMonthsByYearIdFlow(yearId: Long): Flow<List<Month>> =
        queries
            .selectMonthsByYearId(yearId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toMonth(emptyList()) } }

    suspend fun getMonthsByYearId(yearId: Long): List<MonthEntity> = queries.selectMonthsByYearId(yearId).executeAsList()

    suspend fun getMonthById(id: Long): MonthEntity? = queries.selectMonthById(id).executeAsOneOrNull()

    suspend fun getMonthByUuid(uuid: String): MonthEntity? = queries.selectMonthByUuid(uuid).executeAsOneOrNull()

    suspend fun updateMonthTotals(
        monthId: Long,
        income: Double,
        expense: Double,
        balance: Double,
    ) {
        queries.updateMonthTotals(
            income = income,
            expense = expense,
            balance = balance,
            id = monthId,
        )
    }

    suspend fun deleteMonth(id: Int) {
        queries.deleteMonthById(id.toLong())
    }

    // ==================== ENTRY ====================

    suspend fun insertEntry(
        entry: Entry,
        monthId: Long,
    ): Long =
        database.transactionWithResult {
            queries.insertEntry(
                uuid = entry.uuid,
                name = entry.name,
                value_ = entry.value,
                date = entry.date,
                type = entry.type.name,
                month_id = monthId,
                category_id = entry.category.id.toLong(),
            )
            queries.lastInsertRowId().executeAsOne()
        }

    fun getEntriesByMonthIdFlow(monthId: Long): Flow<List<Entry>> =
        queries
            .selectEntriesWithCategoryByMonthId(monthId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toEntryWithCategory() } }

    suspend fun getEntriesByMonthId(monthId: Long): List<EntryEntity> = queries.selectEntriesByMonthId(monthId).executeAsList()

    suspend fun getEntryByUuid(uuid: String): Entry? {
        val entity = queries.selectEntryByUuid(uuid).executeAsOneOrNull() ?: return null
        val category = getCategoryById(entity.category_id.toInt()) ?: Category()
        return entity.toEntry(category)
    }

    suspend fun updateEntry(entry: Entry) {
        queries.updateEntry(
            name = entry.name,
            value_ = entry.value,
            date = entry.date,
            type = entry.type.name,
            category_id = entry.category.id.toLong(),
            id = entry.id.toLong(),
        )
    }

    suspend fun updateEntryMonthId(
        entryId: Int,
        monthId: Long,
    ) {
        queries.updateEntryMonthId(
            month_id = monthId,
            id = entryId.toLong(),
        )
    }

    suspend fun deleteEntry(id: Int) {
        queries.deleteEntryById(id.toLong())
    }

    // ==================== AGGREGATIONS ====================

    suspend fun calculateMonthTotals(monthId: Long): Triple<Double, Double, Double> {
        val income = queries.sumIncomeByMonthId(monthId).executeAsOne()
        val expense = queries.sumExpenseByMonthId(monthId).executeAsOne()
        val balance = income - expense
        return Triple(income, expense, balance)
    }

    // ==================== MAPPERS ====================

    private fun UserEntity.toUser(): User =
        User(
            uuid = uuid,
            name = name,
            currencyCode = currency_code,
        )

    private fun CategoryEntity.toCategory(): Category =
        Category(
            id = id.toInt(),
            name = name,
            color = hexToColor(color_hex),
            stringKey = string_key,
        )

    private fun YearEntity.toYear(months: List<Month>): Year =
        Year(
            id = id.toInt(),
            uuid = uuid,
            name = name,
            months = months,
        )

    private fun MonthEntity.toMonth(entries: List<Entry>): Month =
        Month(
            id = id.toInt(),
            uuid = uuid,
            name = name,
            income = income,
            expense = expense,
            balance = balance,
            entries = entries,
        )

    private fun EntryEntity.toEntry(category: Category): Entry =
        Entry(
            id = id.toInt(),
            uuid = uuid,
            name = name,
            value = value_,
            date = date,
            type = parseEntryType(type),
            category = category,
        )

    private fun SelectEntriesWithCategoryByMonthId.toEntryWithCategory(): Entry =
        Entry(
            id = id.toInt(),
            uuid = uuid,
            name = name,
            value = value_,
            date = date,
            type = parseEntryType(type),
            category =
                Category(
                    id = category_id_.toInt(),
                    name = category_name,
                    color = hexToColor(category_color_hex),
                    stringKey = category_string_key,
                ),
        )

    private fun parseEntryType(raw: String): EntryType =
        try {
            EntryType.valueOf(raw)
        } catch (_: IllegalArgumentException) {
            EntryType.EXPENSE
        }

    private fun colorToHex(color: androidx.compose.ui.graphics.Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return "#${red.toString(
            16,
        ).padStart(2, '0').uppercase()}${green.toString(16).padStart(2, '0').uppercase()}${blue.toString(16).padStart(2, '0').uppercase()}"
    }

    private fun hexToColor(hex: String): androidx.compose.ui.graphics.Color {
        val cleaned = hex.removePrefix("#")
        if (cleaned.length != 6 && cleaned.length != 8) return com.minhagrana.ui.theme.gray
        return runCatching {
            val rgb = cleaned.takeLast(6)
            val alpha = if (cleaned.length == 8) cleaned.substring(0, 2).toInt(16) else 255
            val red = rgb.substring(0, 2).toInt(16)
            val green = rgb.substring(2, 4).toInt(16)
            val blue = rgb.substring(4, 6).toInt(16)
            androidx.compose.ui.graphics
                .Color(red = red, green = green, blue = blue, alpha = alpha)
        }.getOrDefault(com.minhagrana.ui.theme.gray)
    }
}
