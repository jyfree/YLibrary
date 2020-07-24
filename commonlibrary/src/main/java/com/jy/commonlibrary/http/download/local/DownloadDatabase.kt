package com.jy.commonlibrary.http.download.local

import com.jy.baselibrary.utils.BaseUtils
import com.jy.litedb.annotation.Database
import com.jy.litedb.api.DBConfig
import com.jy.litedb.api.LiteDatabase
import com.jy.litedb.api.YLite

@Database(entities = [DownInfo::class], name = "download.db", version = 1)
abstract class DownloadDatabase : LiteDatabase() {

    abstract fun getDownLoadDao(): DownloadDao

    private object DBHolder {
        val instance = YLite.databaseBuilder(
            BaseUtils.getApp(),
            DownloadDatabase::class.java
        )
            .setOpenDexFileLoader(DownloadDBConfig.openDexFileLoader)
            .setDBConfig(DBConfig.beginBuilder().setOpenCache(true).build())
            .build()
    }

    companion object {
        val instance: DownloadDatabase
            get() = DBHolder.instance
    }
}