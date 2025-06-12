package com.example.todoist_core.cache

class AndroidCacheContainer: CacheContainer {
    override suspend fun <T> write(key: CacheContainerKey, data: T) {
        TODO("Not yet implemented")
    }

    override suspend fun <T> read(key: CacheContainerKey): T? {
        TODO("Not yet implemented")
    }

    override suspend fun remove(key: CacheContainerKey) {
        TODO("Not yet implemented")
    }
}

actual fun getCacheContainer(): CacheContainer = AndroidCacheContainer()