package com.example.todoist_core.cache

class AndroidCacheContainer: CacheContainer {
    override suspend fun <T> write(key: CacheContainerKey, data: T) {
    }

    override suspend fun <T> read(key: CacheContainerKey): T? {
        return null
    }

    override suspend fun remove(key: CacheContainerKey) {
    }
}

actual fun getCacheContainer(): CacheContainer = AndroidCacheContainer()