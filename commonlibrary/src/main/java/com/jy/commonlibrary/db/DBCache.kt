package com.jy.commonlibrary.db

import android.util.LruCache
import java.util.*

/**

 * @Author Administrator
 * @Date 2019/10/29-14:35
 * @TODO
 */
class DBCache(maxSize: Int) : LruCache<String, ArrayList<*>>(maxSize) {

    companion object {
        fun init(): DBCache {
            return DBCache(getDefaultLruCacheSize())
        }

        private fun getDefaultLruCacheSize(): Int {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            return maxMemory / 10
        }
    }

    fun getList(key: String): ArrayList<*>? {
        return get(key)
    }

    fun putList(key: String, data: ArrayList<*>) {
        put(key, data)
    }
}