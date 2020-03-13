package com.jy.sociallibrary.ext.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.manager.SDKThreadManager
import com.jy.sociallibrary.utils.SDKLogUtils


/**
 * @Author Administrator
 * @Date 2019/11/12-11:03
 * @TODO
 */
class SDKLoginActivity : AppCompatActivity(), Observer<StatusBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        StatusLiveData.getInstance().observe(this, this);
        ExtLogin.instance.sdkLoginManager.behavior(this, savedInstanceState)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            SDKThreadManager.getMainHandler().post {
                ExtLogin.instance.sdkLoginManager.checkLogin(this@SDKLoginActivity, intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ExtLogin.instance.sdkLoginManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onChanged(t: StatusBean?) {
        t?.let {
            when (it.status) {
                SDKConstants.LoginStatus.WX_LOGIN_SUCCEED -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信登录授权--成功--code", it.code)
                    ExtLogin.instance.sdkLoginManager.onResultToWXAuthSuccess(this, it.code)
                }
                SDKConstants.LoginStatus.WX_LOGIN_FAIL -> {
                    SDKLogUtils.e("接收到MutableLiveData--微信登录授权--失败--errCode", it.errCode)
                    ExtLogin.instance.sdkLoginManager.onResultToWXAuthFail(this, it.errCode)
                }
                SDKConstants.LoginStatus.WX_LOGIN_CANCEL -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信登录授权--取消")
                    ExtLogin.instance.sdkLoginManager.onResultToWXAuthCancel(this)
                }
            }
        }
    }
}
