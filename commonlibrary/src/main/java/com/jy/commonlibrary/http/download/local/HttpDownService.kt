package com.jy.commonlibrary.http.download.local

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface HttpDownService {
    @Streaming
    @GET
    fun download(@Header("RANGE") start: String, @Url url: String): Observable<ResponseBody>
}