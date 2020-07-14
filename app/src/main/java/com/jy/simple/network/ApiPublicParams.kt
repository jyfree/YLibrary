package com.jy.simple.network

import android.os.Build


/**
 * 公共参数类
 */
object ApiPublicParams {

    /**
     * 获取手机设备型号
     */
    fun getClientModel(): String = Build.MODEL ?: "unknow"

    /**
     * 获取系统OS版本
     */
    fun getOSVersion() = Build.VERSION.RELEASE ?: "unknow"

}