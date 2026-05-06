package com.minhagrana.currency

import com.minhagrana.models.repositories.UserRepository
import com.minhagrana.util.getDeviceCountryCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrencyRepository(
    private val userRepository: UserRepository,
) {
    private val current = MutableStateFlow(Currencies.BRL)

    fun observe(): StateFlow<Currency> = current.asStateFlow()

    suspend fun refreshFromActiveUser() {
        val user = userRepository.getFirstUserOrNull() ?: return
        current.value = Currencies.byCode(user.currencyCode)
    }

    fun resolveFromDevice(): Currency = Currencies.byCountry(getDeviceCountryCode())

    suspend fun setForUser(
        userUuid: String,
        currency: Currency,
    ) {
        val user = userRepository.getUserByUuid(userUuid) ?: return
        userRepository.updateUser(user.copy(currencyCode = currency.code))
        current.value = currency
    }

    fun setLocal(currency: Currency) {
        current.value = currency
    }
}
