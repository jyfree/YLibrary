package com.jy.simple.http.mvp

import com.jy.baselibrary.base.broker.BasePresenter
import com.jy.simple.bean.SendGiftVo
import com.jy.simple.viewmodel.MvpViewModel


/**

 * @Author Administrator
 * @Date 2019/9/27-9:45
 * @TODO
 */
class ApiSimplePresenter : BasePresenter<ApiSimpleContract.View, MvpViewModel>(),
    ApiSimpleContract.Presenter {


    override fun getBanner(showPlace: Int) {
        if (mView == null) return
        mViewModel.call(request = {
            mViewModel.bannerRepository.getBanner(showPlace)
        }, success = {
            mView?.updateInfo(it.data?.toString())
        })
    }

    override fun sendGift(sendGiftVo: SendGiftVo) {
        if (mView == null) return
        mViewModel.call(request = {
            mViewModel.userRepository.sendGift(sendGiftVo)
        }, success = {
            mView?.updateInfo(it.msg)
        })
    }

    override fun getUserRelationId() {
        if (mView == null) return
        mViewModel.callData(request = {
            mViewModel.userRepository.getUserRelationId()
        }, success = {

        })
    }

}