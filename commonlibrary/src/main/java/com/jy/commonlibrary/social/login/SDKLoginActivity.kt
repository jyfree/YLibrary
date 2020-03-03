package com.jy.commonlibrary.social.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jy.baselibrary.utils.YHandlerUtils
import com.jy.commonlibrary.rxbus.RxBus
import com.jy.commonlibrary.rxbus.Subscribe
import com.jy.commonlibrary.rxbus.ThreadMode
import com.jy.commonlibrary.social.SocialConstants


/**
 * @Author Administrator
 * @Date 2019/11/12-11:03
 * @TODO
 */
class SDKLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        RxBus.getDefault().register(this)
        ExtLogin.instance.sdkLoginManager.behavior(this, savedInstanceState)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            YHandlerUtils.runOnUiThread(Runnable {
                ExtLogin.instance.sdkLoginManager.checkLogin(this@SDKLoginActivity, intent)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ExtLogin.instance.sdkLoginManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().unregister(this)
    }

    @SuppressLint("微信登录授权回调--成功")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_SUCCEED, threadMode = ThreadMode.MAIN)
    fun rxBusWXLoginSucceed(code: String) {
        ExtLogin.instance.sdkLoginManager.onResultToWXAuthSuccess(this, code)
    }

    @SuppressLint("微信登录授权回调--失败")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_FAIL, threadMode = ThreadMode.MAIN)
    fun rxBusWXLoginFail(errCode: Int) {
        ExtLogin.instance.sdkLoginManager.onResultToWXAuthFail(this, errCode)
    }

    @SuppressLint("微信登录授权回调--取消")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_LOGIN_AUTH_CANCEL, threadMode = ThreadMode.MAIN)
    fun rxBusWXLoginCancel() {
        ExtLogin.instance.sdkLoginManager.onResultToWXAuthCancel(this)
    }
}
