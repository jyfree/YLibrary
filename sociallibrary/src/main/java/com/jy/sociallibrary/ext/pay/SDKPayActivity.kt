package com.jy.sociallibrary.ext.pay

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jy.sociallibrary.SDKThreadManager


/**

 * @Author Administrator
 * @Date 2019/12/26-16:52
 * @TODO
 */
class SDKPayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        SDKPay.instance.sdkPayManager.behavior(this, savedInstanceState)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            SDKThreadManager.getMainHandler().post {
                SDKPay.instance.sdkPayManager.checkPay(this@SDKPayActivity, intent)
            }
        }
    }

}