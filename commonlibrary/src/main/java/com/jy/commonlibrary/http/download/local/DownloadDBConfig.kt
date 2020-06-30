package com.jy.commonlibrary.http.download.local

object DownloadDBConfig {

    var openDexFileLoader = true

    /**
     * true ：使用dex初始化数据库表信息
     * false：使用插件初始化数据库信息
     */
    fun init(isOpenDexFileLoaderDB: Boolean) {
        openDexFileLoader = isOpenDexFileLoaderDB
    }
}