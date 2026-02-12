package com.app.minhagrana.di

import com.app.minhagrana.database.DatabaseDriverFactory
import com.app.minhagrana.database.DatabaseHelper
import com.app.minhagrana.database.MinhaGranaDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { MinhaGranaDatabase(get()) }
    single { DatabaseHelper(get()) }
}
