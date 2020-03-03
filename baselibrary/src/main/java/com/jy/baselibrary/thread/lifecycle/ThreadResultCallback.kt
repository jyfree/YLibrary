package com.jy.baselibrary.thread.lifecycle

/**

 * @Author Administrator
 * @Date 2019/10/24-18:20
 * @TODO
 */
open class ThreadResultCallback<T>(val doNext: (T?) -> Unit) {
    fun forResult(t: T?) {
        doNext.invoke(t)
    }
}