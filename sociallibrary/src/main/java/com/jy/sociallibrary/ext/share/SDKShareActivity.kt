package com.jy.sociallibrary.ext.share

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jy.sociallibrary.SDKThreadManager


/**

 * @Author Administrator
 * @Date 2019/12/27-10:31
 * @TODO
 */
class SDKShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        SDKShare.instance.sdkShareManager.behavior(this, savedInstanceState)
        SDKShare.instance.sdkShareManager.doResultIntent(intent)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            SDKThreadManager.getMainHandler().post {
                SDKShare.instance.sdkShareManager.checkShare(this@SDKShareActivity, intent)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        SDKShare.instance.sdkShareManager.doResultIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SDKShare.instance.sdkShareManager.onActivityResult(requestCode, resultCode, data)
    }
}