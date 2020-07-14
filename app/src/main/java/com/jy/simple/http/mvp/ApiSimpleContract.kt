package com.jy.simple.http.mvp

import com.jy.baselibrary.base.broker.BaseContract
import com.jy.simple.bean.SendGiftVo
import com.jy.simple.viewmodel.MvpViewModel


/**

 * @Author Administrator
 * @Date 2019/9/27-9:46
 * @TODO
 */
interface ApiSimpleContract {
    interface View : BaseContract.BaseView {
        fun updateInfo(msg: String?)
    }

    interface Presenter : BaseContract.BasePresenter<View, MvpViewModel> {
        fun getBanner(showPlace: Int)
        fun sendGift(sendGiftVo: SendGiftVo)
        fun getUserRelationId()
    }
}