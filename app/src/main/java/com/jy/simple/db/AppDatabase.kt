package com.jy.simple.db

import com.jy.baselibrary.utils.BaseUtils
import com.jy.litedb.annotation.Database
import com.jy.litedb.api.DBConfig
import com.jy.litedb.api.LiteDatabase
import com.jy.litedb.api.YLite

@Database(entities = [TestInfo::class], name = "app.db", version = 3)
abstract class AppDatabase : LiteDatabase() {
    abstract fun getTestInfoDao(): TestInfoDao

    private object DBHolder {
        val instance = YLite.databaseBuilder(
            BaseUtils.getApp(),
            AppDatabase::class.java
        )
            .setOpenDexFileLoader(false)
            .setDBConfig(DBConfig.beginBuilder().setOpenCache(true).build())
            .build()
    }

    companion object {
        @get:Synchronized
        val instance: AppDatabase
            get() = DBHolder.instance
    }
}