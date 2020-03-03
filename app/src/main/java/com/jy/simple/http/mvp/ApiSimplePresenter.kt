package com.jy.simple.http.mvp

import com.jy.baselibrary.base.presenter.BasePresenter
import com.jy.commonlibrary.http.RxObserver
import com.jy.simple.http.bean.SendGiftVo


/**

 * @Author Administrator
 * @Date 2019/9/27-9:45
 * @TODO
 */
class ApiSimplePresenter : BasePresenter<ApiSimpleContract.View, ApiSimpleContract.Model>(), ApiSimpleContract.Presenter {

    override fun sendGift(sendGiftVo: SendGiftVo) {
        if (mView == null) return
        mView?.showPopWindowLoading(true)
        mMode.sendGift(RxObserver(doNext = { it ->
            mView?.showPopWindowLoading(false)
            mView?.updateInfo(it.msg)
        }, doError = { _, _ ->
            mView?.showPopWindowLoading(false)
        }), mView!!.lifecycleProvider, sendGiftVo)
    }

    override fun getBanner(showPlace: Int) {
        if (mView == null) return
        mView?.showPopWindowLoading(true)
        mMode.getBanner(RxObserver(doNext = { it ->
            mView?.showPopWindowLoading(false)
            mView?.updateInfo(it.data?.toString())
        }, doError = { _, _ ->
            mView?.showPopWindowLoading(false)
        }), mView!!.lifecycleProvider, showPlace)
    }


}