package com.jy.sociallibrary.ext.share

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.SDKThreadManager
import com.jy.sociallibrary.utils.SDKLogUtils


/**

 * @Author Administrator
 * @Date 2019/12/27-10:31
 * @TODO
 */
class SDKShareActivity : AppCompatActivity(), Observer<StatusBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        StatusLiveData.getInstance().observe(this, this);
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

    override fun onChanged(t: StatusBean?) {
        t?.let {
            when (it.status) {
                SDKConstants.ShareStatus.WX_SHARE_SUCCESS -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信分享--成功")
                    SDKShare.instance.sdkShareManager.onResultToWXShareSuccess(this)
                }
                SDKConstants.ShareStatus.WX_SHARE_FAIL -> {
                    SDKLogUtils.e("接收到MutableLiveData--微信分享--失败--errCode", it.errCode)
                    SDKShare.instance.sdkShareManager.onResultToWXShareFail(this, it.errCode)
                }
                SDKConstants.ShareStatus.WX_SHARE_CANCEL -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信分享--取消")
                    SDKShare.instance.sdkShareManager.onResultToWXShareCancel(this)
                }
            }
        }
    }
}