package com.minhagrana.di

import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.models.repositories.CategoryRepository
import com.minhagrana.models.repositories.EntryRepository
import com.minhagrana.models.repositories.MonthRepository
import com.minhagrana.models.repositories.UserRepository
import com.minhagrana.models.repositories.YearRepository
import com.minhagrana.models.repositories.impl.CategoryRepositoryImpl
import com.minhagrana.models.repositories.impl.EntryRepositoryImpl
import com.minhagrana.models.repositories.impl.MonthRepositoryImpl
import com.minhagrana.models.repositories.impl.UserRepositoryImpl
import com.minhagrana.models.repositories.impl.YearRepositoryImpl
import com.minhagrana.models.services.MonthResolver
import org.koin.dsl.module

val repositoryModule =
    module {
        single<CategoryRepository> { CategoryRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<MonthRepository> { MonthRepositoryImpl(get()) }
        single<YearRepository> { YearRepositoryImpl(get(), get(), get()) }
        single<EntryRepository> { EntryRepositoryImpl(get(), get()) }
        single { DatabaseInitializer(get(), get(), get()) }
        single { MonthResolver(get(), get()) }
    }
