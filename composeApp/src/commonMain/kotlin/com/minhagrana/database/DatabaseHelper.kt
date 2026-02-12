package com.minhagrana.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.entities.User
import com.minhagrana.entities.Year
import com.minhagrana.entities.getDefaultColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseHelper(private val database: MinhaGranaDatabase) {

    private val queries = database.minhaGranaDatabaseQueries

    // ==================== USER ====================

    suspend fun insertUser(user: User): Long {
        queries.insertUser(
            uuid = user.uuid,
            name = user.name,
            email = user.email,
            password = user.password,
            balance_visibility = user.balanceVisibility
        )
        return queries.lastInsertRowId().executeAsOne()
    }

    fun getAllUsersFlow(): Flow<List<User>> {
        return queries.selectAllUsers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toUser() } }
    }

    suspend fun getUserByEmail(email: String): User? {
        return queries.selectUserByEmail(email).executeAsOneOrNull()?.toUser()
    }

    suspend fun getUserByUuid(uuid: String): User? {
        return queries.selectUserByUuid(uuid).executeAsOneOrNull()?.toUser()
    }

    suspend fun updateUser(user: User) {
        queries.updateUser(
            name = user.name,
            email = user.email,
            password = user.password,
            balance_visibility = user.balanceVisibility,
            id = user.id.toLong()
        )
    }

    suspend fun deleteUser(id: Int) {
        queries.deleteUserById(id.toLong())
    }

    // ==================== CATEGORY ====================

    suspend fun insertCategory(category: Category): Long {
        queries.insertCategory(
            name = category.name,
            color_hex = colorToHex(category.color)
        )
        return queries.lastInsertRowId().executeAsOne()
    }

    fun getAllCategoriesFlow(): Flow<List<Category>> {
        return queries.selectAllCategories()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toCategory() } }
    }

    suspend fun getAllCategories(): List<Category> {
        return queries.selectAllCategories().executeAsList().map { it.toCategory() }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return queries.selectCategoryById(id.toLong()).executeAsOneOrNull()?.toCategory()
    }

    suspend fun getCategoryByName(name: String): Category? {
        return queries.selectCategoryByName(name).executeAsOneOrNull()?.toCategory()
    }

    // ==================== YEAR ====================

    suspend fun insertYear(year: Year, userId: Long): Long {
        queries.insertYear(
            uuid = year.uuid,
            name = year.name,
            user_id = userId
        )
        return queries.lastInsertRowId().executeAsOne()
    }

    fun getYearsByUserIdFlow(userId: Long): Flow<List<Year>> {
        return queries.selectYearsByUserId(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toYear(emptyList()) } }
    }

    suspend fun getYearByUuid(uuid: String): YearEntity? {
        return queries.selectYearByUuid(uuid).executeAsOneOrNull()
    }

    suspend fun deleteYear(id: Int) {
        queries.deleteYearById(id.toLong())
    }

    // ==================== MONTH ====================

    suspend fun insertMonth(month: Month, yearId: Long): Long {
        queries.insertMonth(
            uuid = month.uuid,
            name = month.name,
            income = month.income,
            expense = month.expense,
            balance = month.balance,
            year_id = yearId
        )
        return queries.lastInsertRowId().executeAsOne()
    }

    fun getMonthsByYearIdFlow(yearId: Long): Flow<List<Month>> {
        return queries.selectMonthsByYearId(yearId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toMonth(emptyList()) } }
    }

    suspend fun getMonthsByYearId(yearId: Long): List<MonthEntity> {
        return queries.selectMonthsByYearId(yearId).executeAsList()
    }

    suspend fun getMonthByUuid(uuid: String): MonthEntity? {
        return queries.selectMonthByUuid(uuid).executeAsOneOrNull()
    }

    suspend fun updateMonthTotals(monthId: Long, income: Double, expense: Double, balance: Double) {
        queries.updateMonthTotals(
            income = income,
            expense = expense,
            balance = balance,
            id = monthId
        )
    }

    suspend fun deleteMonth(id: Int) {
        queries.deleteMonthById(id.toLong())
    }

    // ==================== ENTRY ====================

    suspend fun insertEntry(entry: Entry, monthId: Long): Long {
        queries.insertEntry(
            uuid = entry.uuid,
            name = entry.name,
            value_ = entry.value,
            date = entry.date,
            repeat = entry.repeat.toLong(),
            type = entry.type.name,
            month_id = monthId,
            category_id = entry.category.id.toLong()
        )
        return queries.lastInsertRowId().executeAsOne()
    }

    fun getEntriesByMonthIdFlow(monthId: Long): Flow<List<Entry>> {
        return queries.selectEntriesByMonthId(monthId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { entity ->
                    val category = getCategoryById(entity.category_id.toInt()) ?: Category()
                    entity.toEntry(category)
                }
            }
    }

    suspend fun getEntriesByMonthId(monthId: Long): List<EntryEntity> {
        return queries.selectEntriesByMonthId(monthId).executeAsList()
    }

    suspend fun updateEntry(entry: Entry) {
        queries.updateEntry(
            name = entry.name,
            value_ = entry.value,
            date = entry.date,
            repeat = entry.repeat.toLong(),
            type = entry.type.name,
            category_id = entry.category.id.toLong(),
            id = entry.id.toLong()
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

    private fun UserEntity.toUser(): User = User(
        id = id.toInt(),
        uuid = uuid,
        name = name,
        email = email,
        password = password,
        balanceVisibility = balance_visibility
    )

    private fun CategoryEntity.toCategory(): Category = Category(
        id = id.toInt(),
        name = name,
        color = getDefaultColor(name)
    )

    private fun YearEntity.toYear(months: List<Month>): Year = Year(
        id = id.toInt(),
        uuid = uuid,
        name = name,
        months = months
    )

    private fun MonthEntity.toMonth(entries: List<Entry>): Month = Month(
        id = id.toInt(),
        uuid = uuid,
        name = name,
        income = income,
        expense = expense,
        balance = balance,
        entries = entries
    )

    private fun EntryEntity.toEntry(category: Category): Entry = Entry(
        id = id.toInt(),
        uuid = uuid,
        name = name,
        value = value_,
        date = date,
        repeat = repeat.toInt(),
        type = EntryType.valueOf(type),
        category = category
    )

    private fun colorToHex(color: androidx.compose.ui.graphics.Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return "#${red.toString(16).padStart(2, '0').uppercase()}${green.toString(16).padStart(2, '0').uppercase()}${blue.toString(16).padStart(2, '0').uppercase()}"
    }
}
