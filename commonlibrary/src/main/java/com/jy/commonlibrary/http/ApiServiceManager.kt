package com.jy.commonlibrary.http

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.jy.commonlibrary.BaseDomainConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Api管理工具类，通过该类创建相应的api-service类
 */
object ApiServiceManager {

    private val gson = GsonBuilder().registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ -> Date(json.asJsonPrimitive.asLong) }).create()
    private val mRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BaseDomainConfig.URL_REQUEST_DEFAULT)
            .client(HttpClientManager.mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun <T> create(service: Class<T>): T = mRetrofit.create(service)
}