package com.jy.commonlibrary.social.share

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
 * @Date 2019/12/27-10:31
 * @TODO
 */
class SDKShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        RxBus.getDefault().register(this)
        ExtShare.instance.sdkShareManager.behavior(this, savedInstanceState)
        ExtShare.instance.sdkShareManager.doResultIntent(intent)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            YHandlerUtils.runOnUiThread(Runnable {
                ExtShare.instance.sdkShareManager.checkShare(this@SDKShareActivity, intent)
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ExtShare.instance.sdkShareManager.doResultIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ExtShare.instance.sdkShareManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().unregister(this)
    }

    @SuppressLint("微信分享回调--成功")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_SHARE_SUCCESS, threadMode = ThreadMode.MAIN)
    fun rxBusWXShareSucceed() {
        ExtShare.instance.sdkShareManager.onResultToWXShareSuccess(this)
    }

    @SuppressLint("微信分享回调--失败")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_SHARE_FAIL, threadMode = ThreadMode.MAIN)
    fun rxBusWXShareFail(errCode: Int) {
        ExtShare.instance.sdkShareManager.onResultToWXShareFail(this, errCode)
    }

    @SuppressLint("微信分享回调--取消")
    @Subscribe(code = SocialConstants.RxBus.CODE_WX_SHARE_CANCEL, threadMode = ThreadMode.MAIN)
    fun rxBusWXShareCancel() {
        ExtShare.instance.sdkShareManager.onResultToWXShareCancel(this)
    }
}