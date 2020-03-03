package com.jy.commonlibrary.social.base

import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.rxbus.RxBus
import com.jy.commonlibrary.social.SocialConstants
import com.jy.sociallibrary.wx.SDKWXPayEntryActivity

/**

 * @Author Administrator
 * @Date 2019/9/29-18:47
 * @TODO 实现微信支付需要继承此类
 */
abstract class ExtWXPayEntryActivity : SDKWXPayEntryActivity() {
    override fun paySucceed() {
        //TODO 支付成功，发送RxBus
        YLogUtils.i("微信支付--成功")
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_PAY_SUCCESS)
    }

    override fun payCancel() {
        //TODO 取消支付
        YLogUtils.i("微信支付--取消")
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_PAY_CANCEL)
    }

    override fun payFail(errCode: Int) {
        //TODO 支付失败 errCode
        YLogUtils.e("微信支付--失败--errCode", errCode)
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_PAY_FAIL, errCode)
    }


}