package com.jy.baselibrary.thread.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**

 * @Author Administrator
 * @Date 2019/10/24-17:41
 * @TODO 线程监听器，绑定生命周期
 */
open class LifecycleThreadListener(private val thread: Thread, private val cancelEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) :
    LifecycleObserver {

    var isDestroy = false

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() = handleEvent(Lifecycle.Event.ON_PAUSE)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() = handleEvent(Lifecycle.Event.ON_STOP)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() = handleEvent(Lifecycle.Event.ON_DESTROY)

    private fun handleEvent(e: Lifecycle.Event) {

        if (e == cancelEvent) {
            isDestroy = true
            thread.interrupt()
        }
    }
}