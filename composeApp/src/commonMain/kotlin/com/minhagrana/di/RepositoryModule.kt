package com.minhagrana.di

import com.minhagrana.database.DatabaseInitializer
import com.minhagrana.repository.CategoryRepository
import com.minhagrana.repository.EntryRepository
import com.minhagrana.repository.MonthRepository
import com.minhagrana.repository.UserRepository
import com.minhagrana.repository.YearRepository
import com.minhagrana.repository.impl.CategoryRepositoryImpl
import com.minhagrana.repository.impl.EntryRepositoryImpl
import com.minhagrana.repository.impl.MonthRepositoryImpl
import com.minhagrana.repository.impl.UserRepositoryImpl
import com.minhagrana.repository.impl.YearRepositoryImpl
import org.koin.dsl.module

val repositoryModule =
    module {
        single<CategoryRepository> { CategoryRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<MonthRepository> { MonthRepositoryImpl(get()) }
        single<YearRepository> { YearRepositoryImpl(get(), get(), get()) }
        single<EntryRepository> { EntryRepositoryImpl(get(), get()) }
        single { DatabaseInitializer(get(), get(), get()) }
    }
