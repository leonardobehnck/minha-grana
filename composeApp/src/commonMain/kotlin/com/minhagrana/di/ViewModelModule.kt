package com.minhagrana.di

import com.minhagrana.annualbalance.AnnualBalanceViewModel
import com.minhagrana.entries.EntriesViewModel
import com.minhagrana.entries.EntryViewModel
import com.minhagrana.home.HomeViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory { HomeViewModel(get(), get()) }
        factory { AnnualBalanceViewModel(get(), get()) }
        factory { EntriesViewModel(get(), get(), get(), get()) }
        factory { EntryViewModel(get(), get()) }
    }
