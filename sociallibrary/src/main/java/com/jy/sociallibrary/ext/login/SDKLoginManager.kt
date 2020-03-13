package com.jy.sociallibrary.ext.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jy.sociallibrary.constant.SDKLoginType
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.listener.OnSocialSdkLoginListener
import com.jy.sociallibrary.manager.SDKLogin
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener


/**
 * @Author Administrator
 * @Date 2019/11/12-11:08
 * @TODO
 */
class SDKLoginManager {

    private var sdkLogin: SDKLogin? = null
    private var loginListener: OnSocialSdkLoginListener? = null
    private var wxListener: WXListener? = null
    private val LOGIN_TYPE = "loginType"


    fun setLoginListener(loginListener: OnSocialSdkLoginListener): SDKLoginManager {
        this.loginListener = loginListener
        return this
    }

    fun setWXListener(wxListener: WXListener): SDKLoginManager {
        this.wxListener = wxListener
        return this
    }

    fun requestQQLogin(context: Context) {
        request(context, SDKLoginType.TYPE_QQ)
    }

    fun requestWXLogin(context: Context) {
        request(context, SDKLoginType.TYPE_WX)
    }

    fun requestWBLogin(context: Context) {
        request(context, SDKLoginType.TYPE_WB)
    }

    fun request(context: Context, loginType: Int) {
        val intent = Intent(context, SDKLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(LOGIN_TYPE, loginType)
        context.startActivity(intent)
    }


    fun behavior(activity: Activity, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            initSdkLogin(activity)
            showSDKProgress(true)
        }
    }

    private fun initSdkLogin(activity: Activity) {
        sdkLogin = SDKLogin(
            activity,
            object : OnSocialSdkLoginListener {
                override fun loginAuthSuccess(type: Int, token: String, info: String) {
                    onDestroy(activity)
                    loginListener?.loginAuthSuccess(type, token, info)
                }

                override fun loginFail(type: Int, error: String) {
                    onDestroy(activity)
                    loginListener?.loginFail(type, error)
                }

                override fun loginCancel(type: Int) {
                    onDestroy(activity)
                    loginListener?.loginCancel(type)
                }
            })
        sdkLogin?.setWXListener {
            onDestroy(activity)
            wxListener?.installWXAPP()
        }
    }

    fun checkLogin(activity: Activity, intent: Intent?) {

        if (intent == null) {
            SDKLogUtils.e("checkLogin intent is null")
            onDestroy(activity)
            return
        }
        if (intent.extras == null) {
            SDKLogUtils.e("checkLogin extras is null")
            onDestroy(activity)
            return
        }

        when (intent.getIntExtra(LOGIN_TYPE, 0)) {
            SDKLoginType.TYPE_QQ -> qqLogin()
            SDKLoginType.TYPE_WB -> wbLogin()
            SDKLoginType.TYPE_WX -> wxLogin()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        sdkLogin?.result2Activity(requestCode, resultCode, data)
    }

    fun onResultToWXAuthSuccess(activity: Activity, message: String?) {
        onDestroy(activity)
        loginListener?.loginAuthSuccess(SDKLoginType.TYPE_WX, "", message)
    }

    fun onResultToWXAuthCancel(activity: Activity) {
        onDestroy(activity)
        loginListener?.loginCancel(SDKLoginType.TYPE_WX)
    }

    fun onResultToWXAuthFail(activity: Activity, errCode: Int) {
        onDestroy(activity)
        loginListener?.loginFail(SDKLoginType.TYPE_WX, "错误码：$errCode")
    }

    private fun qqLogin() {
        sdkLogin?.qqLogin()
    }

    private fun wxLogin() {
        sdkLogin?.wxLogin()
    }

    private fun wbLogin() {
        sdkLogin?.wbLogin()
    }

    private fun showSDKProgress(show: Boolean) {
        sdkLogin?.showProgressDialog(show)
    }

    /**
     * 摧毁本库的 SDKLoginActivity
     */
    private fun onDestroy(activity: Activity?) {
        showSDKProgress(false)
        activity?.finish()
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }
}
