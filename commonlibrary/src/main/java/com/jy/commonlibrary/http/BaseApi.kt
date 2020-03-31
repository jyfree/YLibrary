package com.jy.commonlibrary.http

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

abstract class BaseApi {

    private val gSon = GsonBuilder().registerTypeAdapter(
        Date::class.java,
        JsonDeserializer<Date> { json, _, _ -> Date(json.asJsonPrimitive.asLong) }).create()

    /**
     * 初始化Retrofit
     */
    fun initRetrofit(baseUrl: String): Retrofit {
        val builder = Retrofit.Builder()
        //支持直接格式化json返回Bean对象
        builder.addConverterFactory(GsonConverterFactory.create(gSon))
        //支持RxJava
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        builder.baseUrl(baseUrl)
        val client = setClient()
        if (client != null) {
            builder.client(client)
        }
        return builder.build()
    }

    /**
     * 设置OkHttpClient，添加拦截器等
     *
     * @return 可以返回为null
     */
    protected abstract fun setClient(): OkHttpClient?
}
