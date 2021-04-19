package com.jy.simple.network.api

import com.jy.commonlibrary.http.bean.BaseBean
import com.jy.commonlibrary.http.bean.HttpParam
import com.jy.commonlibrary.http.bean.SingleBaseBean
import com.jy.simple.bean.BannerInfoListVo
import com.jy.simple.network.bean.HttpRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Administrator
 * created at 2018/12/11 15:08
 * description:主页接口
 */
interface ApiSimpleService {
    //获取banner
    @POST("call.do?action=home.banner")
    fun getBanner(@Body httpRequest: HttpRequest): Observable<BannerInfoListVo>

    //送礼
    @POST("call.do?action=roomBehavior.sendGift")
    fun sendGift(@Body httpRequest: HttpRequest): Observable<SingleBaseBean>

    //测试
    @POST("call.do?action=test")
    fun testHttpParam(@Body params: HttpParam): Observable<BannerInfoListVo>

    @POST("user-center/platformuser/tplatformuser/getRelationId")
    fun getUserRelationId(): Observable<BaseBean<String>>
}