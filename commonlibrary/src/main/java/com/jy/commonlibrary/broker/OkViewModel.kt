package com.jy.commonlibrary.broker

import com.jy.baselibrary.base.broker.BaseViewModel
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

    fun <T> call(request: () -> Observable<T>, success: (T) -> Unit) {
        call(request, success, failed = {})
    }

    fun <T> callNoLoading(request: () -> Observable<T>, success: (T) -> Unit) {
        callNoLoading(request, success, failed = {})
    }

    fun <T> callNotLifecycle(request: () -> Observable<T>, success: (T) -> Unit) {
        callNotLifecycle(request, success, failed = {})
    }

    /**
     * 显示菊花，RxHelper.handleSingleResult()方式请求
     *
     */
    fun <T> call(
        request: () -> Observable<T>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        loading.value = true

        request.invoke()
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(getLifeCycleProvide())
            .subscribe(RxObserver<T>(doNext = {
                loading.value = false
                success.invoke(it)
            }, doError = { _, _ ->
                loading.value = false
                failed.invoke()
            }))
    }

    /**
     * 不显示菊花，RxHelper.handleSingleResult()方式请求
     */
    fun <T> callNoLoading(
        request: () -> Observable<T>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        request.invoke()
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(getLifeCycleProvide())
            .subscribe(RxObserver<T>(doNext = {
                success.invoke(it)
            }, doError = { _, _ ->
                failed.invoke()
            }))
    }

    /**
     * 不绑定生命周期，RxHelper.handleSingleResult()方式请求
     */
    fun <T> callNotLifecycle(
        request: () -> Observable<T>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        request.invoke()
            .compose(RxHelper.handleSingleResult())
            .subscribe(RxObserver<T>(doNext = {
                success.invoke(it)
            }, doError = { _, _ ->
                failed.invoke()
            }))
    }

    //********************************RxHelper.handleResult()类型********************************


    fun <T> callData(request: () -> Observable<BaseBean<T>>, success: (T) -> Unit) {
        callData(request, success, failed = {})
    }

    fun <T> callDataNoLoading(request: () -> Observable<BaseBean<T>>, success: (T) -> Unit) {
        callDataNoLoading(request, success, failed = {})
    }

    fun <T> callDataNotLifecycle(request: () -> Observable<BaseBean<T>>, success: (T) -> Unit) {
        callDataNotLifecycle(request, success, failed = {})
    }

    /**
     * 显示菊花，RxHelper.handleResult()方式请求
     */
    fun <T> callData(
        request: () -> Observable<BaseBean<T>>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        loading.value = true
        request.invoke()
            .compose(RxHelper.handleResult())
            .bindToLifecycle(getLifeCycleProvide())
            .subscribe(RxObserver<T>(doNext = {
                loading.value = false
                success.invoke(it)
            }, doError = { _, _ ->
                loading.value = false
                failed.invoke()
            }))
    }

    /**
     * 不显示菊花，RxHelper.handleResult()方式请求
     */
    fun <T> callDataNoLoading(
        request: () -> Observable<BaseBean<T>>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        request.invoke()
            .compose(RxHelper.handleResult())
            .bindToLifecycle(getLifeCycleProvide())
            .subscribe(RxObserver<T>(doNext = {
                success.invoke(it)
            }, doError = { _, _ ->
                failed.invoke()
            }))
    }

    /**
     * 不绑定生命周期，RxHelper.handleResult()方式请求
     */
    fun <T> callDataNotLifecycle(
        request: () -> Observable<BaseBean<T>>,
        success: (T) -> Unit,
        failed: () -> Unit
    ) {
        request.invoke()
            .compose(RxHelper.handleResult())
            .subscribe(RxObserver<T>(doNext = {
                success.invoke(it)
            }, doError = { _, _ ->
                failed.invoke()
            }))
    }
}