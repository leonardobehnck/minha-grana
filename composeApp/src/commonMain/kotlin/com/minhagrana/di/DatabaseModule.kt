package com.minhagrana.di

import com.minhagrana.database.DatabaseDriverFactory
import com.minhagrana.database.DatabaseHelper
import com.minhagrana.database.MinhaGranaDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { MinhaGranaDatabase(get()) }
    single { DatabaseHelper(get()) }
}
