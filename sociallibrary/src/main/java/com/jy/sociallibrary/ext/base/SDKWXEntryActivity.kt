package com.jy.sociallibrary.ext.base

import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXBaseEntryActivity


/**

 * @Author Administrator
 * @Date 2019/9/29-18:45
 * @TODO 实现微信登录|分享需要继承此类
 */
abstract class SDKWXEntryActivity : WXBaseEntryActivity() {

    //****************************登录***************************************
    override fun authSucceed(code: String?) {
        //TODO 登录授权成功
        SDKLogUtils.i("微信登录授权--成功--code", code)
        val statusBean = StatusBean();
        statusBean.code = code
        statusBean.status = SDKConstants.LoginStatus.WX_LOGIN_SUCCEED
        StatusLiveData.getInstance().value = statusBean;
    }

    override fun authCancel() {
        //TODO 取消登录授权
        SDKLogUtils.i("微信登录授权--取消")
        val statusBean = StatusBean();
        statusBean.status = SDKConstants.LoginStatus.WX_LOGIN_CANCEL
        StatusLiveData.getInstance().value = statusBean;
    }

    override fun authFail(errCode: Int) {
        //TODO 授权失败 errCode
        SDKLogUtils.e("微信登录授权--失败--errCode", errCode)
        val statusBean = StatusBean();
        statusBean.errCode = errCode
        statusBean.status = SDKConstants.LoginStatus.WX_LOGIN_FAIL
        StatusLiveData.getInstance().value = statusBean;
    }

    //****************************分享***************************************

    override fun shareSucceed() {
        //TODO 分享微信成功
        SDKLogUtils.i("微信分享--成功")
        val statusBean = StatusBean();
        statusBean.status = SDKConstants.ShareStatus.WX_SHARE_SUCCESS
        StatusLiveData.getInstance().value = statusBean;
    }

    override fun shareCancel() {
        //TODO 取消分享
        SDKLogUtils.i("微信分享--取消")
        val statusBean = StatusBean();
        statusBean.status = SDKConstants.ShareStatus.WX_SHARE_CANCEL
        StatusLiveData.getInstance().value = statusBean;
    }

    override fun shareFail(errCode: Int) {
        //TODO 分享失败 errCode
        SDKLogUtils.e("微信分享--失败--errCode", errCode)
        val statusBean = StatusBean();
        statusBean.errCode = errCode
        statusBean.status = SDKConstants.ShareStatus.WX_SHARE_FAIL
        StatusLiveData.getInstance().value = statusBean;
    }
}