package com.minhagrana.util

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale

actual fun getDeviceCountryCode(): String = NSLocale.currentLocale.countryCode.orEmpty()
