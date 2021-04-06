package com.jy.simple.social.common

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.sociallibrary.ext.login.SDKLoginCommonActivity

class LoginSimpleCommonActivity : SDKLoginCommonActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, LoginSimpleCommonActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_login_activity)
    }

    fun onClickLogin(view: View) {
        when (view.id) {
            R.id.login_2qq -> qqLogin()
            R.id.login_2wx -> wxLogin()
            R.id.login_2wb -> wbLogin()
        }
    }


    override fun onLoginAuthSuccess(type: Int, token: String?, info: String?) {
        YLogUtils.i("登录授权成功--类型：", type, "token", token, "info", info)
    }

    override fun onLoginFail(type: Int, error: String?) {
        YLogUtils.e("登录授权失败--类型：", type, "error", error)
    }

    override fun onLoginCancel(type: Int) {
        YLogUtils.i("取消登录--类型：", type)
    }

    override fun onStartWXApp(isSucceed: Boolean) {
        YLogUtils.e("启动微信成功？", isSucceed)
    }

    override fun onUnInstallWXApp() {
        YLogUtils.e("未安装微信")
    }
}