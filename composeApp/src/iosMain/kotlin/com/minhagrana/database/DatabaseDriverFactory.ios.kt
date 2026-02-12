package com.minhagrana.database

import cash.sqldelight.db.SqlDriver
import cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(MinhaGranaDatabase.Schema, "minhagrana.db")
}
