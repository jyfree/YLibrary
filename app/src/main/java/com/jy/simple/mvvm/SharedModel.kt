package com.jy.simple.mvvm

import com.jy.baselibrary.base.model.BaseModel
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.commonlibrary.http.bean.HttpEntry
import com.jy.simple.http.bean.BannerInfoListVo
import com.jy.simple.http.network.Api
import com.jy.simple.http.network.api.ApiSimpleService
import com.jy.simple.http.network.bean.HttpRequest
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindToLifecycle


/**

 * @Author Administrator
 * @Date 2019/11/4-15:42
 * @TODO
 */
class SharedModel : BaseModel<ApiSimpleService>(ApiSimpleService::class.java) {


    fun <E> getBanner(rxObserver: RxObserver<BannerInfoListVo>, mView: LifecycleProvider<E>, showPlace: Int) {
        Api.simpleInstance.getBanner(HttpRequest.obtainHttpRequest(HttpEntry("showPlace", showPlace)))
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(mView)
            .subscribe(rxObserver)
    }
}