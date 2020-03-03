package com.jy.commonlibrary.http.download.local

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RetryWhenNetworkException : Function<Observable<out Throwable>, Observable<*>> {
    //    retry次数
    private var count = 3
    //    延迟
    private var delay: Long = 3000
    //    叠加延迟
    private var increaseDelay: Long = 3000

    constructor() {

    }

    constructor(count: Int, delay: Long) {
        this.count = count
        this.delay = delay
    }

    constructor(count: Int, delay: Long, increaseDelay: Long) {
        this.count = count
        this.delay = delay
        this.increaseDelay = increaseDelay
    }

    override fun apply(observable: Observable<out Throwable>): Observable<*> {
        return observable
                .zipWith(Observable.range(1, count + 1), BiFunction<Throwable, Int, Wrapper> { t1, t2 ->
                    Wrapper(t1, t2)
                })
                .flatMap { wrapper ->
                    if ((wrapper.throwable is ConnectException
                                    || wrapper.throwable is SocketTimeoutException
                                    || wrapper.throwable is TimeoutException) && wrapper.index < count + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                        Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS)

                    } else Observable.error<Any>(wrapper.throwable)
                }
    }

    private inner class Wrapper internal constructor(val throwable: Throwable, val index: Int)

}