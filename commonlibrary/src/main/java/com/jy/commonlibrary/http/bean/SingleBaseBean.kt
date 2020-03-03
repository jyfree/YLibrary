package com.jy.commonlibrary.http.bean

/**

 * @Author Administrator
 * @Date 2019/9/26-17:06
 * @TODO
 */
open class SingleBaseBean(
    val code: Int = -1,
    val msg: String = "error"
) {
    val isSuccess
        get() = code in 200..299 || code == 0
}