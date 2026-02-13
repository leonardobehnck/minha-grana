package com.minhagrana.di

import org.koin.core.context.startKoin

/**
 * Must be called from Swift (iOSApp.init or AppDelegate) BEFORE any Compose/UI code.
 * Safe to call multiple times: startKoin runs only once.
 * In Swift: KoinInitializer.shared.initKoin()
 */
 
object KoinInitializer {
    private var started = false

    fun start() {
        if (started) return
        started = true
        startKoin {
            modules(iosModule, databaseModule, repositoryModule, viewModelModule)
        }
    }
}
