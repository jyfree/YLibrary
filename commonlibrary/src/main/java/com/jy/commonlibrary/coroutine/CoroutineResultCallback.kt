package com.jy.commonlibrary.coroutine

/**

 * @Author Administrator
 * @Date 2019/10/24-18:20
 * @TODO
 */
open class CoroutineResultCallback<T>(val doNext: (T?) -> Unit) {
    fun forResult(t: T?) {
        doNext.invoke(t)
    }
}