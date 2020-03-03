package com.jy.baselibrary.utils.cache;


import android.util.LruCache;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2019/11/7-14:55
 * @TODO 内存缓存，可设置缓存时长{@link CacheConstants}
 */
public class CacheMemoryUtils implements CacheConstants {

    private static final int DEFAULT_MAX_COUNT = 256;

    private static final Map<String, CacheMemoryUtils> CACHE_MAP = new HashMap<>();

    private final String mCacheKey;
    private final LruCache<String, CacheValue> mMemoryCache;


    public static CacheMemoryUtils getInstance() {
        return getInstance(DEFAULT_MAX_COUNT);
    }


    public static CacheMemoryUtils getInstance(final int maxCount) {
        return getInstance(String.valueOf(maxCount), maxCount);
    }


    public static CacheMemoryUtils getInstance(final String cacheKey, final int maxCount) {
        CacheMemoryUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (CacheMemoryUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheMemoryUtils(cacheKey, new LruCache<String, CacheValue>(maxCount));
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheMemoryUtils(String cacheKey, LruCache<String, CacheValue> memoryCache) {
        mCacheKey = cacheKey;
        mMemoryCache = memoryCache;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 默认缓存
     *
     * @param key
     * @param value
     */
    public void put(@NonNull final String key, final Object value) {
        put(key, value, -1);
    }

    /**
     * @param key
     * @param value
     * @param saveTime 缓存时间（单位秒）
     */
    public void put(@NonNull final String key, final Object value, int saveTime) {
        if (value == null) return;
        long dueTime = saveTime < 0 ? -1 : System.currentTimeMillis() + saveTime * 1000;
        mMemoryCache.put(key, new CacheValue(dueTime, value));
    }


    public <T> T get(@NonNull final String key) {
        return get(key, null);
    }


    /**
     * 获取缓存  若超出缓存时间则自动清除
     *
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T> T get(@NonNull final String key, final T defaultValue) {
        CacheValue val = mMemoryCache.get(key);
        if (val == null) return defaultValue;
        if (val.dueTime == -1 || val.dueTime >= System.currentTimeMillis()) {
            //noinspection unchecked
            return (T) val.value;
        }
        mMemoryCache.remove(key);
        return defaultValue;
    }


    public int getCacheCount() {
        return mMemoryCache.size();
    }


    public Object remove(@NonNull final String key) {
        CacheValue remove = mMemoryCache.remove(key);
        if (remove == null) return null;
        return remove.value;
    }


    public void clear() {
        mMemoryCache.evictAll();
    }

    private static final class CacheValue {
        long dueTime;
        Object value;

        CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}