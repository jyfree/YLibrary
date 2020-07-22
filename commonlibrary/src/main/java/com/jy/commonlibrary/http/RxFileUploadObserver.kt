package com.jy.commonlibrary.http

import com.jy.baselibrary.utils.ToastUtils
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DefaultObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class RxFileUploadObserver<T> : DefaultObserver<T>() {
    private var mDisposable: Disposable? = null
    private var totalLength: Long = 0

    var showToast = true

    fun showToast(showToast: Boolean) {
        this.showToast = showToast
    }

    override fun onNext(t: T) {
        onUploadSuccess(t)
    }

    override fun onError(e: Throwable) {
//        if (!NetworkUtils.isConnected()) {
//            doError("网络不可用...")
//        } else
        if (e is UnknownHostException) {// 未知主机
            doErrorSwitchHost()
        } else if (e is retrofit2.HttpException) {
            if ("HTTP 404 Not Found" == e.message || "HTTP 502 Fiddler - DNS Lookup Failed" == e.message) { // 404 not found
                doErrorSwitchHost()
            } else {
                e.message?.let { doError(it) }
            }
        } else if (e is SocketTimeoutException) {
            doError("请求超时，请稍后再试...")
        } else if (e is ConnectException) {
            doError("网络连接有误，请稍后再试...")
        } else if (e is ApiException) {// 非 200 错误
            e.message?.let { doError(it) }
        } else {
            onUploadFail(e)
        }

        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    override fun onComplete() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun doErrorSwitchHost() {
        if (showToast) {
            ToastUtils.showToast("连接不上服务器...")
        }
    }

    private fun doError(message: String) {
        if (showToast) {
            ToastUtils.showToast(message)
        }
    }

    abstract fun onUploadSuccess(t: T)

    abstract fun onUploadFail(e: Throwable)

    abstract fun onProgress(progress: Int)

    fun onProgressChange(bytesWritten: Long, contentLength: Long) {
        if (totalLength != 0L)
            onProgress((bytesWritten * 100 / totalLength).toInt())
        else
            onProgress((bytesWritten * 100 / contentLength).toInt())
    }

    fun setTotalLength(totalLength: Long) {
        this.totalLength = totalLength
    }
}