package com.jy.commonlibrary.http.download.local

import com.jy.litedb.annotation.Dao
import com.jy.litedb.annotation.Query
import com.jy.litedb.api.IDao

@Dao(entities = DownInfo::class)
interface DownloadDao : IDao<DownInfo> {
    @Query("SELECT * FROM DownInfo WHERE url = ':url'")
    fun queryDownloadInfoByPath(url: String): DownInfo?
}