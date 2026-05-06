package com.minhagrana.util

import java.util.Locale

actual fun getDeviceCountryCode(): String = Locale.getDefault().country.orEmpty()
