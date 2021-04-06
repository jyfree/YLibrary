package com.jy.sociallibrary.ext.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jy.sociallibrary.constant.SDKLoginType
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.helper.LoginHelper
import com.jy.sociallibrary.listener.OnSocialSdkLoginListener
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener

/**
 * @description 实现登录的activity，若继承此activity，则无需在manifest注册透明的SDKLoginActivity
 * @date: 2021/4/6 14:26
 * @author: jy
 */
abstract class SDKLoginCommonActivity : AppCompatActivity(), OnSocialSdkLoginListener, WXListener {

    private var loginHelper: LoginHelper? = null
    var progressView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initSdkLogin()
            registerObserve(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginHelper?.result2Activity(requestCode, resultCode, data)
    }


    private fun initSdkLogin() {
        loginHelper = LoginHelper(this, this)
        loginHelper?.setWXListener(this)
    }

    /**
     * 必需注册LiveData，用于微信回调
     * todo 注意：从任务管理器切换回app是无法收到微信回调的
     */
    private fun registerObserve(owner: LifecycleOwner) {
        StatusLiveData.getInstance().observe(owner, Observer<StatusBean?> {
            it?.let {
                when (it.status) {
                    SDKConstants.LoginStatus.WX_LOGIN_SUCCEED -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信登录授权--成功--code", it.code)
                        loginAuthSuccess(SDKLoginType.TYPE_WX, "", it.code)
                    }
                    SDKConstants.LoginStatus.WX_LOGIN_FAIL -> {
                        SDKLogUtils.e("接收到MutableLiveData--微信登录授权--失败--errCode", it.errCode)
                        loginFail(SDKLoginType.TYPE_WX, "错误码：$it.errCode")
                    }
                    SDKConstants.LoginStatus.WX_LOGIN_CANCEL -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信登录授权--取消")
                        loginCancel(SDKLoginType.TYPE_WX)
                    }
                    else -> {

                    }
                }
            }
        })
    }


    fun qqLogin() {
        showProgress(true)
        loginHelper?.qqLogin()
    }

    fun wxLogin() {
        showProgress(true)
        loginHelper?.wxLogin()
    }

    fun wbLogin() {
        showProgress(true)
        loginHelper?.wbLogin()
    }


    override fun loginCancel(type: Int) {
        reset()
        onLoginCancel(type)
    }

    override fun loginFail(type: Int, error: String?) {
        reset()
        onLoginFail(type, error)
    }

    override fun loginAuthSuccess(type: Int, token: String?, info: String?) {
        reset()
        onLoginAuthSuccess(type, token, info)
    }

    override fun startWX(isSucceed: Boolean) {
        if (isSucceed) {
            showProgress(false)
        } else {
            reset()
        }
        onStartWXApp(isSucceed)
    }

    override fun installWXAPP() {
        SDKLogUtils.e("未安装微信")
        reset()
        onUnInstallWXApp()
    }

    private fun showProgress(show: Boolean) {
        if (progressView == null) {
            loginHelper?.showProgressDialog(show)
        } else {
            progressView?.visibility = if (show) View.VISIBLE else View.GONE
        }
    }


    private fun reset() {
        showProgress(false)
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }

    abstract fun onLoginAuthSuccess(type: Int, token: String?, info: String?)
    abstract fun onLoginFail(type: Int, error: String?)
    abstract fun onLoginCancel(type: Int)
    abstract fun onStartWXApp(isSucceed: Boolean)
    abstract fun onUnInstallWXApp()
}