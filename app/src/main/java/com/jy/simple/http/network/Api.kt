package com.jy.simple.http.network

import com.jy.commonlibrary.http.BaseApi
import com.jy.commonlibrary.http.interceptor.OkHttpClientLogInterceptor
import com.jy.simple.BuildConfig
import com.jy.simple.RequestDomainConfig
import com.jy.simple.http.network.api.ApiSimpleService
import com.jy.simple.http.network.api.ApiUploadSimpleService
import com.jy.simple.http.network.interceptor.OkHttpHeadPublicParamsInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class Api : BaseApi() {

    /**
     * 静态内部类单例
     */
    object ApiHolder {

        private val api = Api()

        val SIMPLE_SERVICE: ApiSimpleService = api.initRetrofit(RequestDomainConfig.getBaseUrl())
            .create(ApiSimpleService::class.java)

        val UPLOAD_SERVICE: ApiUploadSimpleService =
            api.initRetrofit(RequestDomainConfig.getBaseUrl())
                .create(ApiUploadSimpleService::class.java)
    }


    override fun setClient(): OkHttpClient? {
        return initOkHttpClient()
    }

    private fun initOkHttpClient(): OkHttpClient? {
        val httpClientBuild = OkHttpClient.Builder()
        httpClientBuild.addInterceptor(OkHttpHeadPublicParamsInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG)
            httpClientBuild.addInterceptor(OkHttpClientLogInterceptor())
        return httpClientBuild.build()
    }

    companion object {
        val simpleInstance: ApiSimpleService
            get() = ApiHolder.SIMPLE_SERVICE
        val uploadInstance: ApiUploadSimpleService
            get() = ApiHolder.UPLOAD_SERVICE
    }

}
