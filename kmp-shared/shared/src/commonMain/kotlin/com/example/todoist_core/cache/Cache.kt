package com.example.todoist_core.cache

interface CacheContainer {
    suspend fun <T> write(key: CacheContainerKey, data: T)
    suspend fun <T> read(key: CacheContainerKey): T?
    suspend fun remove(key: CacheContainerKey)
}

enum class CacheContainerKey {
    ACCESS_TOKEN {
        override fun toString(): String {
            return "access_token"
        }
    }
}

expect fun getCacheContainer(): CacheContainer