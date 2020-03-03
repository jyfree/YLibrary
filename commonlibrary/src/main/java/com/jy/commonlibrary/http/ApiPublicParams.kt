package com.jy.commonlibrary.http

import com.jy.baselibrary.BuildConfig


/**
 * 公共参数类
 */
object ApiPublicParams {

    private const val API_PARAM_TOKEN = "token"
    private const val API_PARAM_OS = "os"
    private const val API_PARAM_OS_VERSION = "osVersion"
    private const val API_PARAM_VERSION = "version"
    private const val API_PARAM_TIME = "time"

    fun getRequestPubMap(): Map<String, String> {
        val params = mutableMapOf<String, String>()

//        params[API_PARAM_TOKEN] = getApiToken()
        params[API_PARAM_OS] = "2" // 0-默认1-苹果 2-安卓
        params[API_PARAM_OS_VERSION] = android.os.Build.VERSION.RELEASE
        params[API_PARAM_VERSION] = BuildConfig.VERSION_NAME
        params[API_PARAM_TIME] = System.currentTimeMillis().toString()

        return params
    }
}