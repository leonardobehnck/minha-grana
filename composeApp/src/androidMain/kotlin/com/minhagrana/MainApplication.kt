package com.minhagrana

import android.app.Application
import com.minhagrana.di.androidModule
import com.minhagrana.di.databaseModule
import com.minhagrana.di.repositoryModule
import com.minhagrana.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(androidModule, databaseModule, repositoryModule, viewModelModule)
        }
    }
}
