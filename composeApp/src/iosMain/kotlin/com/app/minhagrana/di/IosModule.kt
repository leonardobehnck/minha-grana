package com.app.minhagrana.di

import com.app.minhagrana.database.DatabaseDriverFactory
import org.koin.dsl.module

val iosModule = module {
    single { DatabaseDriverFactory() }
}
