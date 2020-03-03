package com.jy.commonlibrary.http

import com.jy.baselibrary.utils.BaseUtils
import com.jy.baselibrary.utils.ToastUtils
import com.jy.baselibrary.utils.YLogUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RxObserver<T> : Observer<T> {
    private var doError: ((e: Throwable, message: String) -> Unit)? = null
    private var doNull: (() -> Unit)? = null
    private var doNext: ((t: T) -> Unit?)? = null

    var showToast = true

    constructor(doError: ((e: Throwable, message: String) -> Unit)? = null, doNext: ((t: T) -> Unit?)? = null) {
        this.doError = doError
        this.doNext = doNext
    }

    constructor(doNull: (() -> Unit)? = null, doNext: ((t: T) -> Unit?)? = null) {
        this.doNull = doNull
        this.doNext = doNext
    }

    constructor(doNext: ((t: T) -> Unit?)? = null) {
        this.doNext = doNext
    }

    constructor(showToast: Boolean) {
        this.showToast = showToast
    }

    private var mDisposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
    }

    override fun onComplete() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    override fun onNext(t: T) {
        doNext?.invoke(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
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
        } else if (e is ApiResultDataNullException) {
            doNull?.invoke()
        } else {
            doError("请求失败，请稍后再试...")
        }

        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
        doError?.invoke(e, e.message.toString())
        YLogUtils.e("Http error --> ${e.message} || ${e.stackTrace}")
    }

    private fun doErrorSwitchHost() {
        if (showToast) {
            ToastUtils.showToast(BaseUtils.getApp(), "连接不上服务器...")
        }
    }

    private fun doError(message: String) {
        if (showToast) {
            ToastUtils.showToast(BaseUtils.getApp(), message)
        }
    }
}