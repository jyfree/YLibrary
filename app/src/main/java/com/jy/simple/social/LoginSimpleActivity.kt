package com.jy.simple.social

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.sociallibrary.constant.SDKLoginType
import com.jy.sociallibrary.ext.login.SDKLogin
import com.jy.sociallibrary.ext.login.SDKLoginManager
import com.jy.sociallibrary.listener.OnSocialSdkLoginListener
import com.jy.sociallibrary.wx.WXListener


/**

 * @Author Administrator
 * @Date 2019/11/12-14:40
 * @TODO
 */
class LoginSimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, LoginSimpleActivity::class.java)
        }
    }

    var sdkLoginManager: SDKLoginManager? = null

    override fun initLayoutID(): Int = R.layout.simple_login_activity

    override fun initUI(savedInstanceState: Bundle?) {
    }


    fun onClickLogin(view: View) {
        when (view.id) {
            R.id.login_2qq -> request(SDKLoginType.TYPE_QQ)
            R.id.login_2wx -> request(SDKLoginType.TYPE_WX)
            R.id.login_2wb -> request(SDKLoginType.TYPE_WB)
        }
    }

    private fun request(loginType: Int) {
        if (sdkLoginManager == null) {
            sdkLoginManager = SDKLogin.instance.sdkLoginManager.setLoginListener(object :
                OnSocialSdkLoginListener {
                override fun loginAuthSuccess(type: Int, token: String?, info: String?) {
                    YLogUtils.i("登录授权成功--类型：", type, "token", token, "info", info)
                }

                override fun loginFail(type: Int, error: String?) {
                    YLogUtils.e("登录授权失败--类型：", type, "error", error)
                }

                override fun loginCancel(type: Int) {
                    YLogUtils.i("取消登录--类型：", type)
                }
            }).setWXListener(object : WXListener {
                override fun startWX(isSucceed: Boolean) {
                    YLogUtils.e("启动微信成功？", isSucceed)
                }

                override fun installWXAPP() {
                    YLogUtils.e("未安装微信")
                }

            }).registerObserve(this)
        }
        sdkLoginManager?.request(this, loginType)
    }
}