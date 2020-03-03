package com.jy.commonlibrary.db

/**
 * @Author Administrator
 * @Date 2019/10/29-15:07
 * @TODO
 */
class DBCacheManager private constructor() {

    val dbCache: DBCache = DBCache.init()

    private object CacheHolder {
        val instance = DBCacheManager()
    }

    companion object {

        val instance: DBCacheManager
            @Synchronized get() = CacheHolder.instance
    }
}
