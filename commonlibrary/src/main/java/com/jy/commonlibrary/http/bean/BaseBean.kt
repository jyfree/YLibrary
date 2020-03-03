package com.jy.commonlibrary.http.bean

/**

 * @Author Administrator
 * @Date 2019/9/26-17:05
 * @TODO
 */
open class BaseBean<T>(
        val code: Int = -1,
        val msg: String = "error"
) {
    val data: T? = null
    val isSuccess
        get() = code in 200..299 || code == 0
}