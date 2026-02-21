package com.minhagrana.di

import com.minhagrana.models.annualbalance.AnnualBalanceViewModel
import com.minhagrana.models.entries.EntriesViewModel
import com.minhagrana.models.entries.EntryViewModel
import com.minhagrana.models.home.HomeViewModel
import com.minhagrana.models.newentry.NewEntryViewModel
import com.minhagrana.models.onboarding.OnboardingViewModel
import com.minhagrana.models.profile.ProfileViewModel
import com.minhagrana.models.root.RootViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory { RootViewModel(get()) }
        factory { HomeViewModel(get(), get()) }
        factory { AnnualBalanceViewModel(get(), get()) }
        factory { EntriesViewModel(get(), get(), get(), get()) }
        factory { EntryViewModel(get(), get(), get(), get()) }
        factory { NewEntryViewModel(get(), get(), get()) }
        factory { OnboardingViewModel(get()) }
        factory { ProfileViewModel(get()) }
    }
