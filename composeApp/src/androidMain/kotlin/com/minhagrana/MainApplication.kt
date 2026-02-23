package com.minhagrana

import android.app.Application
import com.minhagrana.di.androidModule
import com.minhagrana.di.databaseModule
import com.minhagrana.di.repositoryModule
import com.minhagrana.di.viewModelModule
import com.minhagrana.database.DatabaseInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.get

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(androidModule, databaseModule, repositoryModule, viewModelModule)
        }

        val isDebug = false
        if (isDebug) {
            CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
                get<DatabaseInitializer>(DatabaseInitializer::class.java).initialize(isDebug = true)
            }
        }
    }
}
