package com.jy.commonlibrary.http.bean


class HttpParam private constructor() : HashMap<String, Any>() {
    companion object {
        fun obtain(): HttpParam = HttpParam()

        fun obtain(key: String, value: Any): HttpParam {
            val httpParam = HttpParam()
            httpParam[key] = value
            return httpParam
        }
    }
}