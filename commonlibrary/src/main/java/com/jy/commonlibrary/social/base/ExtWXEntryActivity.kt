package com.jy.commonlibrary.social.base

import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.rxbus.RxBus
import com.jy.commonlibrary.social.SocialConstants
import com.jy.sociallibrary.wx.SDKWXEntryActivity


/**

 * @Author Administrator
 * @Date 2019/9/29-18:45
 * @TODO 实现微信登录|分享需要继承此类
 */
abstract class ExtWXEntryActivity : SDKWXEntryActivity() {

    override fun authSucceed(code: String?) {
        //TODO 登录授权成功，发送RxBus 请求微信登录接口
        YLogUtils.i("微信登录授权--成功--code", code)
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_SUCCEED, code)
    }

    override fun authCancel() {
        //TODO 取消登录授权
        YLogUtils.i("微信登录授权--取消")
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_CANCEL)
    }

    override fun authFail(errCode: Int) {
        //TODO 授权失败 errCode
        YLogUtils.e("微信登录授权--失败--errCode", errCode)
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_FAIL, errCode)
    }

    override fun shareSucceed() {
        //TODO 分享微信成功，发送RxBus
        YLogUtils.i("微信分享--成功")
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_SHARE_SUCCESS)
    }

    override fun shareCancel() {
        //TODO 取消分享
        YLogUtils.i("微信分享--取消")
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_SHARE_CANCEL)
    }

    override fun shareFail(errCode: Int) {
        //TODO 分享失败 errCode
        YLogUtils.e("微信分享--失败--errCode", errCode)
        RxBus.getDefault().send(SocialConstants.RxBus.CODE_WX_SHARE_FAIL, errCode)
    }
}