package com.jy.commonlibrary.http

import com.jy.baselibrary.utils.BaseUtils
import com.jy.baselibrary.utils.ToastUtils
import com.jy.baselibrary.utils.YLogUtils
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**

 * @Author Administrator
 * @Date 2019/11/5-15:38
 * @TODO 处理异常Throwable
 */
object RxDoError {

    fun onError(e: Throwable, showToast: Boolean = true) {
        e.printStackTrace()
//        if (!NetworkUtils.isConnected()) {
//            doError("网络不可用...")
//        } else
        if (e is UnknownHostException) {// 未知主机
            doErrorSwitchHost(showToast)
        } else if (e is retrofit2.HttpException) {
            if ("HTTP 404 Not Found" == e.message || "HTTP 502 Fiddler - DNS Lookup Failed" == e.message) { // 404 not found
                doErrorSwitchHost(showToast)
            } else {
                e.message?.let { doError(it, showToast) }
            }
        } else if (e is SocketTimeoutException) {
            doError("请求超时，请稍后再试...", showToast)
        } else if (e is ConnectException) {
            doError("网络连接有误，请稍后再试...", showToast)
        } else if (e is ApiException) {// 非 200 错误
            e.message?.let { doError(it, showToast) }
        } else if (e is ApiResultDataNullException) {
//            doNull?.invoke()
        } else {
            doError("请求失败，请稍后再试...", showToast)
        }
        YLogUtils.e("Http error --> ${e.message} || ${e.stackTrace}")
    }

    private fun doErrorSwitchHost(showToast: Boolean) {
        if (showToast) {
            ToastUtils.showToast(BaseUtils.getApp(), "连接不上服务器...")
        }
    }

    private fun doError(message: String, showToast: Boolean) {
        if (showToast) {
            ToastUtils.showToast(BaseUtils.getApp(), message)
        }
    }
}