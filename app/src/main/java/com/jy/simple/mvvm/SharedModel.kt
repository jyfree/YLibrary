package com.jy.simple.mvvm

import com.jy.baselibrary.base.model.BaseModel
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.bean.HttpEntry
import com.jy.simple.http.network.api.ApiSimpleService
import com.jy.simple.http.bean.BannerInfoListVo
import com.jy.simple.http.network.Api
import com.jy.simple.http.network.bean.HttpRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**

 * @Author Administrator
 * @Date 2019/11/4-15:42
 * @TODO
 */
class SharedModel : BaseModel<ApiSimpleService>(ApiSimpleService::class.java) {


    fun getBanner(showPlace: Int): Observable<BannerInfoListVo> {
        return Api.simpleInstance.getBanner(HttpRequest.obtainHttpRequest(HttpEntry("showPlace", showPlace)))
                .compose(RxHelper.handleSingleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}