package com.minhagrana

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform