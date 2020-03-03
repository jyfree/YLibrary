package com.jy.commonlibrary.http.download.local.listener

import okhttp3.Interceptor
import okhttp3.Response

class DownloadInterceptor(val listener: DownloadProgressListener?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(DownloadResponseBody(originalResponse.body, listener))
            .build()
    }
}