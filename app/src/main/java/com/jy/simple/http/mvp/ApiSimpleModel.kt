package com.jy.simple.http.mvp


import com.jy.baselibrary.base.model.BaseModel
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.commonlibrary.http.bean.HttpEntry
import com.jy.commonlibrary.http.bean.SingleBaseBean
import com.jy.simple.http.bean.BannerInfoListVo
import com.jy.simple.http.bean.SendGiftVo
import com.jy.simple.http.network.Api
import com.jy.simple.http.network.api.ApiSimpleService
import com.jy.simple.http.network.bean.HttpRequest
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindToLifecycle


/**

 * @Author Administrator
 * @Date 2019/9/27-11:46
 * @TODO
 */
class ApiSimpleModel : BaseModel<ApiSimpleService>(ApiSimpleService::class.java), ApiSimpleContract.Model {

    override fun <E> sendGift(rxObserver: RxObserver<SingleBaseBean>, mView: LifecycleProvider<E>, sendGiftVo: SendGiftVo) {
        Api.simpleInstance.sendGift(HttpRequest.obtainHttpRequest(sendGiftVo))
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(mView)
            .subscribe(rxObserver)
    }

    override fun <E> getBanner(rxObserver: RxObserver<BannerInfoListVo>, mView: LifecycleProvider<E>, showPlace: Int) {
        Api.simpleInstance.getBanner(HttpRequest.obtainHttpRequest(HttpEntry("showPlace", showPlace)))
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(mView)
            .subscribe(rxObserver)

//        Api.simpleInstance.testHttpParam(HttpParam.obtain("showPlace", showPlace))
//            .compose(RxHelper.handleSingleResult())
//            .bindToLifecycle(mView)
//            .subscribe(rxObserver)
    }
}