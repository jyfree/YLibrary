package com.jy.baselibrary.utils

import android.os.Handler
import android.os.Looper

/**
 * @Author jy
 * @Date 2019/8/8-16:00
 * @TODO handler工具类
 */
object YHandlerUtils {
    private var sHandler: Handler? = null

    private fun getHandler(): Handler {
        if (sHandler == null) {
            synchronized(YHandlerUtils::class.java) {
                if (sHandler == null) {
                    sHandler = Handler(Looper.getMainLooper())
                }
            }
        }
        return sHandler!!
    }

    fun runOnBackThread(runnable: Runnable, delay: Int) {
        getHandler().postDelayed({
            val thread = Thread(runnable)
            thread.start()
        }, delay * 1000L)
    }

    fun runOnBackThread(runnable: Runnable) {
        getHandler().post {
            val thread = Thread(runnable)
            thread.start()
        }
    }

    fun runOnUiThread(runnable: Runnable) {
        getHandler().post(runnable)
    }

    fun runOnUiThread(runnable: Runnable, delay: Int) {
        getHandler().postDelayed(runnable, delay * 1000L)
    }

    fun runOnUiThread(runnable: Runnable, delay: Long) {
        getHandler().postDelayed(runnable, delay)
    }
}