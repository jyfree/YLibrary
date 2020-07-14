package com.jy.simple.repository

import com.jy.commonlibrary.http.bean.HttpEntry
import com.jy.commonlibrary.http.bean.HttpParam
import com.jy.simple.bean.BannerInfoListVo
import com.jy.simple.network.Api
import com.jy.simple.network.bean.HttpRequest
import io.reactivex.Observable


/**

 * @Author Administrator
 * @Date 2019/11/4-15:42
 * @TODO banner资源
 */
class BannerRepository {

    fun getBanner(showPlace: Int): Observable<BannerInfoListVo> {
        return Api.simpleInstance.getBanner(
            HttpRequest.obtainHttpRequest(
                HttpEntry("showPlace", showPlace)
            )
        )
    }

    fun testHttpParam(showPlace: Int): Observable<BannerInfoListVo> {
        return Api.simpleInstance.testHttpParam(HttpParam.obtain("showPlace", showPlace))

    }
}