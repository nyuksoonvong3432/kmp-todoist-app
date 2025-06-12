package com.example.todoist_core.cache

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import platform.Foundation.valueForKey

class IOSCacheContainer: CacheContainer {
    override suspend fun <T> write(key: CacheContainerKey, data: T) {
        NSUserDefaults.standardUserDefaults().setValue(data, key.toString())
    }

    override suspend fun <T> read(key: CacheContainerKey): T? {
        return (NSUserDefaults.standardUserDefaults().valueForKey(key.toString()) as? T)
    }

    override suspend fun remove(key: CacheContainerKey) {
        NSUserDefaults.standardUserDefaults().removeObjectForKey(key.toString())
    }
}

actual fun getCacheContainer(): CacheContainer = IOSCacheContainer()