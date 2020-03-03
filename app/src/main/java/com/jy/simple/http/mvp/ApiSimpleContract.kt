package com.jy.simple.http.mvp

import com.jy.baselibrary.base.contract.BaseContract
import com.jy.commonlibrary.http.RxObserver
import com.jy.commonlibrary.http.bean.SingleBaseBean
import com.jy.simple.http.bean.BannerInfoListVo
import com.jy.simple.http.bean.SendGiftVo
import com.trello.rxlifecycle2.LifecycleProvider


/**

 * @Author Administrator
 * @Date 2019/9/27-9:46
 * @TODO
 */
interface ApiSimpleContract {
    interface View : BaseContract.BaseView {
        fun updateInfo(msg: String?)
    }

    interface Presenter : BaseContract.BasePresenter<View, Model> {
        fun getBanner(showPlace: Int)
        fun sendGift(sendGiftVo: SendGiftVo)
    }

    interface Model : BaseContract.BaseModel {
        fun <E> getBanner(rxObserver: RxObserver<BannerInfoListVo>, mView: LifecycleProvider<E>, showPlace: Int)

        fun <E> sendGift(rxObserver: RxObserver<SingleBaseBean>, mView: LifecycleProvider<E>, sendGiftVo: SendGiftVo)
    }
}