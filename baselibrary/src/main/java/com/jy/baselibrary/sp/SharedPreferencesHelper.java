package com.jy.baselibrary.sp;

import android.content.SharedPreferences;

import com.jy.baselibrary.thread.ThreadManage;
import com.jy.baselibrary.utils.YLogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * @Author Administrator
 * @Date 2019/10/14-10:45
 * @TODO
 */
public class SharedPreferencesHelper {
    private final String TAG = "sp";
    //这个锁主要是为了锁住拷贝数据的过程，当进行数据拷贝的时候，不允许任何写入操作
    private final ReadWriteLock copyLock = new ReentrantReadWriteLock();
    //需要同步
    private final AtomicBoolean needSync = new AtomicBoolean(false);
    //同步中
    private final AtomicBoolean syncing = new AtomicBoolean(false);

    //内存缓存
    private final Map<String, Object> keyValueMap = new ConcurrentHashMap<>();
    //sp
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    //**************************************************************
    private void put(String key, Object value) {
        copyLock.readLock().lock();
        keyValueMap.put(key, value);
        copyLock.readLock().unlock();
    }

    public boolean containsKey(String key) {
        return keyValueMap.containsKey(key);
    }

    public Object get(String key) {
        return keyValueMap.get(key);
    }
    //***************************************************************************

    /**
     * 加载sp数据到内存缓存
     */
    public void applyLoad() {
        if (syncing.get()) {
            YLogUtils.INSTANCE.iTag(TAG, "正在同步，取消reload");
            return;
        }
        ThreadManage.getInstance().getLoaderEngine().submit(new ReloadTask(sharedPreferences));
    }

    /**
     * 加载sp数据线程
     */
    private class ReloadTask implements Runnable {
        private SharedPreferences sharedPreferences;

        ReloadTask(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            YLogUtils.INSTANCE.iTag(TAG, "获取sp数据--缓存到内存--开始");
            keyValueMap.clear();
            Map<String, ?> map = sharedPreferences.getAll();
            if (map != null) {
                keyValueMap.putAll(map);
            }
            YLogUtils.INSTANCE.iFormatTag(TAG, "获取sp数据--缓存到内存--结束---time：%s ms---size：%d",
                    System.currentTimeMillis() - startTime, keyValueMap.size());
        }
    }

    //***************************************************************
    public void saveDataAndSyncTask(String keyName, Object value) {
        put(keyName, value);
        postSyncTask();
    }

    public void saveDataAndSyncTaskOfMap(Map<String, Object> dataMap) {
        copyLock.readLock().lock();
        keyValueMap.putAll(dataMap);
        copyLock.readLock().unlock();
        postSyncTask();
    }

    private synchronized void postSyncTask() {
        //设置需要同步
        needSync.compareAndSet(false, true);
        //判断是否同步中
        if (syncing.get()) {
//            YLogUtil.INSTANCE.iTag(TAG, "正在同步");
            return;
        }
        //执行同步
        ThreadManage.getInstance().getLoaderEngine().submit(new SyncTask(sharedPreferences));
    }

    private class SyncTask implements Runnable {

        private SharedPreferences.Editor edit;

        SyncTask(SharedPreferences sharedPreferences) {
            this.edit = sharedPreferences.edit();
        }

        @Override
        public void run() {

            //不需要同步
            if (!needSync.get()) {
                return;
            }

            //先把syncing标记置为true
            syncing.compareAndSet(false, true);

            //拷贝内存数据，copy的过程中不允许写入
            copyLock.writeLock().lock();
            Map<String, Object> storeMap = new HashMap<>(keyValueMap);
            copyLock.writeLock().unlock();

            //把needSync置为false，如果在此之后有数据写入，则需要重新同步
            needSync.compareAndSet(true, false);

            YLogUtils.INSTANCE.iTag(TAG, "同步开始--size", storeMap.size());

            //同步数据
            save(edit, storeMap);

            //解除同步过程
            syncing.compareAndSet(true, false);

//            YLogUtil.INSTANCE.iTag(TAG, "此次同步完成");

            //如果数据被更改，则需要重新同步
            if (needSync.get()) {
//                YLogUtil.INSTANCE.iTag(TAG, "需要继续同步");
                postSyncTask();
            } else {
//                YLogUtil.INSTANCE.iTag(TAG, "全部同步完毕");
            }
        }
    }

    private void save(SharedPreferences.Editor edit, Map<String, Object> dataMap) {
        for (Map.Entry<String, Object> next : dataMap.entrySet()) {
            String key = next.getKey();
            Object value = next.getValue();
            if (value == null) {
                continue;
            }
//            YLogUtil.INSTANCE.iFormatTag(TAG, "保存sp数据--key：%s---value：%s", key, value);
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                edit.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                edit.putLong(key, (Long) value);
            } else {
                edit.putStringSet(key, (Set<String>) value);
            }
        }
        edit.apply();
    }
}
