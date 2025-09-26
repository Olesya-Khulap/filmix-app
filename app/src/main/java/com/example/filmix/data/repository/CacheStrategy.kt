package com.example.filmix.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheStrategy @Inject constructor() {

    companion object {
        const val CACHE_EXPIRY_HOURS = 6
        const val CACHE_EXPIRY_MILLIS = CACHE_EXPIRY_HOURS * 60 * 60 * 1000L
    }

    fun shouldRefreshCache(lastUpdateTime: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - lastUpdateTime > CACHE_EXPIRY_MILLIS
    }

    fun getCacheExpiryTime(): Long {
        return System.currentTimeMillis() - CACHE_EXPIRY_MILLIS
    }
}
