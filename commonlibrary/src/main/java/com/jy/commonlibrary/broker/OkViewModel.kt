package com.jy.commonlibrary.broker

import com.jy.baselibrary.base.broker.BaseViewModel
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.http.ApiException
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.commonlibrary.http.bean.BaseBean
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable

/**
 * @description 封装viewModel网络请求
 * @date: 2020/7/14 10:07
 * @author: jy
 */
open class OkViewModel : BaseViewModel() {

    //********************************RxHelper.handleSingleResult()类型****************************

    /**
     * 显示菊花，RxHelper.handleSingleResult()方式请求
     *
     */
    fun <T> call(
        request: () -> Observable<T>,
        success: (T) -> Unit,
        failed: (Int, String) -> Unit = { code, message -> YLogUtils.e("request error->code：$code，message：$message") },
        needBindLifecycle: Boolean = true
    ) {
        loading.value = true

        var observer = request.invoke().compose(RxHelper.handleSingleResult())
        if (needBindLifecycle) {
            observer.bindToLifecycle(getLifeCycleProvide()).also { observer = it }
        }
        observer.subscribe(RxObserver<T>(doNext = {
            loading.value = false
            success.invoke(it)
        }, doError = { e, message ->
            loading.value = false
            if (e is ApiException)
                failed.invoke(e.code, message)
            else {
                failed.invoke(-1, message)
            }
        }))
    }

    /**
     * 不显示菊花，RxHelper.handleSingleResult()方式请求
     */
    fun <T> callNoLoading(
        request: () -> Observable<T>,
        success: (T) -> Unit,
        failed: (Int, String) -> Unit = { code, message -> YLogUtils.e("request error->code：$code，message：$message") },
        needBindLifecycle: Boolean = true
    ) {
        var observer = request.invoke().compose(RxHelper.handleSingleResult())
        if (needBindLifecycle) {
            observer.bindToLifecycle(getLifeCycleProvide()).also { observer = it }
        }
        observer.subscribe(RxObserver<T>(doNext = {
            success.invoke(it)
        }, doError = { e, message ->
            if (e is ApiException)
                failed.invoke(e.code, message)
            else {
                failed.invoke(-1, message)
            }
        }))
    }


    //********************************RxHelper.handleResult()类型********************************


    /**
     * 显示菊花，RxHelper.handleResult()方式请求
     */
    fun <T> callData(
        request: () -> Observable<BaseBean<T>>,
        success: (T) -> Unit,
        failed: (Int, String) -> Unit = { code, message -> YLogUtils.e("request error->code：$code，message：$message") },
        needBindLifecycle: Boolean = true
    ) {
        loading.value = true
        var observer = request.invoke().compose(RxHelper.handleResult())
        if (needBindLifecycle) {
            observer.bindToLifecycle(getLifeCycleProvide()).also { observer = it }
        }
        observer.subscribe(RxObserver<T>(doNext = {
            loading.value = false
            success.invoke(it)
        }, doError = { e, message ->
            loading.value = false
            if (e is ApiException)
                failed.invoke(e.code, message)
            else {
                failed.invoke(-1, message)
            }
        }))
    }

    /**
     * 不显示菊花，RxHelper.handleResult()方式请求
     */
    fun <T> callDataNoLoading(
        request: () -> Observable<BaseBean<T>>,
        success: (T) -> Unit,
        failed: (Int, String) -> Unit = { code, message -> YLogUtils.e("request error->code：$code，message：$message") },
        needBindLifecycle: Boolean = true
    ) {
        var observer = request.invoke().compose(RxHelper.handleResult())
        if (needBindLifecycle) {
            observer.bindToLifecycle(getLifeCycleProvide()).also { observer = it }
        }
        observer.subscribe(RxObserver<T>(doNext = {
            success.invoke(it)
        }, doError = { e, message ->
            if (e is ApiException)
                failed.invoke(e.code, message)
            else {
                failed.invoke(-1, message)
            }
        }))
    }
}