package com.jy.commonlibrary.db

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import com.jy.baselibrary.utils.YLogUtils
import java.util.concurrent.atomic.AtomicInteger

/**
 * Administrator
 * created at 2018/11/7 15:09
 * TODO:数据库管理类
 * 数据库框架性能对比：https://android.ctolib.com/AlexeyZatsepin-Android-ORM-benchmark.html
 */
class DBManager {

    private val mOpenCounter = AtomicInteger()
    private var mDatabase: SQLiteDatabase? = null

    @Synchronized
    fun openDatabase(): SQLiteDatabase {

        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = try {
                mDatabaseHelper?.writableDatabase
            } catch (e: Exception) {
                e.printStackTrace()
                YLogUtils.e("打开数据库出错", e.message)
                mDatabaseHelper?.readableDatabase
            }

        }
        return mDatabase!!

    }


    @Synchronized
    fun closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            try {
                mDatabase?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                YLogUtils.e("关闭数据库出错", e.message)
            }

        }
    }

    companion object {

        private var instance: DBManager? = null
        private var mDatabaseHelper: BaseOpenHelper? = null

        @Synchronized
        fun initializeInstance(helper: BaseOpenHelper) {
            if (instance == null) {
                instance = DBManager()
                mDatabaseHelper = helper
                //多线程读写
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mDatabaseHelper!!.setWriteAheadLoggingEnabled(true)
                }
            }
        }

        @Synchronized
        fun getInstance(): DBManager {
            if (instance == null) {
                throw IllegalStateException(DBManager::class.java.simpleName + " is not initialized, call initializeInstance(..) method first.")
            }
            return instance!!
        }
    }
}
