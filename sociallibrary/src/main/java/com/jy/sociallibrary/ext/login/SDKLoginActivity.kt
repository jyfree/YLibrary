package com.jy.sociallibrary.ext.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jy.sociallibrary.SDKThreadManager


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
        SDKLogin.instance.sdkLoginManager.behavior(this, savedInstanceState)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            SDKThreadManager.getMainHandler().post {
                SDKLogin.instance.sdkLoginManager.checkLogin(this@SDKLoginActivity, intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SDKLogin.instance.sdkLoginManager.onActivityResult(requestCode, resultCode, data)
    }
}
