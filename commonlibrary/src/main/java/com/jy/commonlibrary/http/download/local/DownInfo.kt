package com.jy.commonlibrary.http.download.local

import com.jy.commonlibrary.http.download.local.listener.HttpDownOnNextListener
import com.jy.litedb.annotation.DBEntity
import com.jy.litedb.annotation.Scope


@DBEntity
class DownInfo {
    @Scope(isPrimaryKey = true, isAutoKey = true)
    var id: Long? = null//主键
    var savePath: String? = null //存储位置
    var countLength: Long = 0 //文件总长度
    var readLength: Long = 0 //下载长度
    @Scope(isFilter = true)
    var service: HttpDownService? = null //下载唯一的HttpService
    @Scope(isFilter = true)
    var listener: HttpDownOnNextListener<*>? = null //回调监听
    var connectionTime = 15 //超时设置
    var stateInt: Int = 0 //state状态数据库保存
    @Scope(isCompareField = true)
    var url: String? = null //url
    var updateProgress: Boolean = false //是否需要实时更新下载进度,避免线程的多次切换

    constructor() {
    }

    constructor(url: String?) {
        this.url = url
    }

    constructor(
        savePath: String?,
        countLength: Long,
        readLength: Long,
        connectionTime: Int,
        stateInt: Int,
        url: String?,
        updateProgress: Boolean
    ) {
        this.savePath = savePath
        this.countLength = countLength
        this.readLength = readLength
        this.connectionTime = connectionTime
        this.stateInt = stateInt
        this.url = url
        this.updateProgress = updateProgress
    }

    fun setState(state: DownState) {
        stateInt = state.state
    }

    fun getState(): DownState {
        return when (stateInt) {
            0 -> DownState.START
            1 -> DownState.DOWN
            2 -> DownState.PAUSE
            3 -> DownState.STOP
            4 -> DownState.ERROR
            5 -> DownState.FINISH
            else -> DownState.FINISH
        }
    }

    fun <T> getHttpDownOnNextListener(): HttpDownOnNextListener<T> =
        listener as HttpDownOnNextListener<T>
}