package com.jy.simple.network.interceptor

import com.jy.simple.network.ApiPublicParams
import okhttp3.Interceptor
import okhttp3.Response

class OkHttpHeadPublicParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("clientMode", ApiPublicParams.getClientModel())
            .addHeader("osVersion", ApiPublicParams.getOSVersion())

        return chain.proceed(builder.build())
    }
}