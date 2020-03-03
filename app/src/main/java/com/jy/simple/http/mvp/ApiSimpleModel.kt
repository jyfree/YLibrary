package com.jy.simple.http.mvp


import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.commonlibrary.http.bean.HttpEntry
import com.jy.commonlibrary.http.bean.SingleBaseBean
import com.jy.simple.http.api.ApiSimpleService
import com.jy.simple.http.base.BaseModel
import com.jy.simple.http.bean.BannerInfoListVo
import com.jy.simple.http.bean.SendGiftVo
import com.jy.simple.http.bean.base.HttpRequest
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindToLifecycle


/**

 * @Author Administrator
 * @Date 2019/9/27-11:46
 * @TODO
 */
class ApiSimpleModel : BaseModel<ApiSimpleService>(ApiSimpleService::class.java), ApiSimpleContract.Model {

    override fun <E> sendGift(rxObserver: RxObserver<SingleBaseBean>, mView: LifecycleProvider<E>, sendGiftVo: SendGiftVo) {
        serviceManager.sendGift(HttpRequest.obtainHttpRequest(sendGiftVo))
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(mView)
            .subscribe(rxObserver)
    }

    override fun <E> getBanner(rxObserver: RxObserver<BannerInfoListVo>, mView: LifecycleProvider<E>, showPlace: Int) {
        serviceManager.getBanner(HttpRequest.obtainHttpRequest(HttpEntry("showPlace", showPlace)))
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(mView)
            .subscribe(rxObserver)
    }
}