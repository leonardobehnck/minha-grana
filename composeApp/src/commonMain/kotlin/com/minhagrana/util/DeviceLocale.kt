package com.minhagrana.util

/**
 * ISO 3166-1 alpha-2 country code from the device locale (e.g. "BR", "US"). Empty string if unavailable.
 */
expect fun getDeviceCountryCode(): String
