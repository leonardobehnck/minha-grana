package com.minhagrana.di

import com.minhagrana.models.annualbalance.AnnualBalanceViewModel
import com.minhagrana.models.entries.EntriesViewModel
import com.minhagrana.models.entries.EntryViewModel
import com.minhagrana.models.home.HomeViewModel
import com.minhagrana.models.onboarding.OnboardingViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory { HomeViewModel(get(), get()) }
        factory { AnnualBalanceViewModel(get(), get()) }
        factory { EntriesViewModel(get(), get(), get(), get()) }
        factory { EntryViewModel(get(), get()) }
        factory { OnboardingViewModel(get()) }
    }
