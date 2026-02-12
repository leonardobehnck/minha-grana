package com.app.minhagrana.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(iosModule, databaseModule)
    }
}
