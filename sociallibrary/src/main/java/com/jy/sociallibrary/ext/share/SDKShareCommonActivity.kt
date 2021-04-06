package com.jy.sociallibrary.ext.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jy.sociallibrary.R
import com.jy.sociallibrary.bean.SDKShareChannel
import com.jy.sociallibrary.constant.SDKSharePlatform
import com.jy.sociallibrary.dialog.SDKShareDialog
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.helper.ShareHelper
import com.jy.sociallibrary.listener.OnSocialSdkShareListener
import com.jy.sociallibrary.media.BaseMediaObject
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener

/**
 * @description 实现分享的activity，若继承此activity，则无需在manifest注册透明的SDKShareActivity
 * @date: 2021/4/6 14:24
 * @author: jy
 */
abstract class SDKShareCommonActivity : AppCompatActivity(), OnSocialSdkShareListener, WXListener {

    private var shareHelper: ShareHelper? = null
    private var sharePlatform: Int = SDKSharePlatform.WX_CB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initShare()
            registerObserve(this)
        }
        shareHelper?.doResultIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        shareHelper?.doResultIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        shareHelper?.result2Activity(requestCode, resultCode, data)
    }

    private fun initShare() {
        shareHelper = ShareHelper(this, this)
        shareHelper?.setWXListener(this)
    }

    /**
     * 必需注册LiveData，用于微信回调
     * todo 注意：从任务管理器切换回app是无法收到微信回调的
     */
    private fun registerObserve(owner: LifecycleOwner) {
        StatusLiveData.getInstance().observe(owner, Observer<StatusBean?> {
            it?.let {
                when (it.status) {
                    SDKConstants.ShareStatus.WX_SHARE_SUCCESS -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信分享--成功")
                        shareSuccess(sharePlatform)
                    }
                    SDKConstants.ShareStatus.WX_SHARE_FAIL -> {
                        SDKLogUtils.e("接收到MutableLiveData--微信分享--失败--errCode", it.errCode)
                        shareFail(sharePlatform, "错误码：$it.errCode")
                    }
                    SDKConstants.ShareStatus.WX_SHARE_CANCEL -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信分享--取消")
                        shareCancel(sharePlatform)
                    }
                    else -> {

                    }
                }
            }
        })
    }

    fun requestShare(media: BaseMediaObject, platformArray: Array<Int>) {
        showShareDialog(this, media, platformArray)
    }

    fun requestShare(media: BaseMediaObject) {
        val platformArray = arrayOf(
            SDKSharePlatform.WX_FRIENDS,
            SDKSharePlatform.WX_CB,
            SDKSharePlatform.QQ_FRIENDS,
            SDKSharePlatform.QQ_QZONE,
            SDKSharePlatform.WB
        )
        showShareDialog(this, media, platformArray)
    }

    fun requestShare(media: BaseMediaObject, sharePlatform: Int) {
        this.sharePlatform = sharePlatform
        shareHelper?.share(sharePlatform, media)
    }

    private fun showShareDialog(
        context: Context,
        media: BaseMediaObject,
        platformArray: Array<Int>
    ) {

        val sdkShareDialog = SDKShareDialog(
            context,
            R.style.social_Theme_dialog,
            getShareChannelData(context, platformArray),
            SDKShareDialog.OnSDKShareListener {
                requestShare(media, it.platform)
            })
        sdkShareDialog.initSystemUI()
        sdkShareDialog.show()
    }

    private fun getShareChannelData(
        context: Context,
        platformArray: Array<Int>
    ): ArrayList<SDKShareChannel> {

        val sdkShareChannels = ArrayList<SDKShareChannel>()

        for (position in platformArray.indices) {
            when (platformArray[position]) {
                SDKSharePlatform.WX_FRIENDS -> {
                    sdkShareChannels.add(
                        SDKShareChannel(
                            SDKSharePlatform.WX_FRIENDS,
                            R.drawable.social_sdk_logo_wechat,
                            context.getString(R.string.social_sdk_share_2wechat)
                        )
                    )
                }
                SDKSharePlatform.QQ_FRIENDS -> {
                    sdkShareChannels.add(
                        SDKShareChannel(
                            SDKSharePlatform.QQ_FRIENDS,
                            R.drawable.social_sdk_logo_qq,
                            context.getString(R.string.social_sdk_share_2qq)
                        )
                    )
                }

                SDKSharePlatform.WX_CB -> {
                    sdkShareChannels.add(
                        SDKShareChannel(
                            SDKSharePlatform.WX_CB,
                            R.drawable.social_sdk_logo_wechatmoments,
                            context.getString(R.string.social_sdk_share_2wechatmoments)
                        )
                    )
                }

                SDKSharePlatform.QQ_QZONE -> {
                    sdkShareChannels.add(
                        SDKShareChannel(
                            SDKSharePlatform.QQ_QZONE,
                            R.drawable.social_sdk_logo_qzone,
                            context.getString(R.string.social_sdk_share_2qzone)
                        )
                    )
                }

                SDKSharePlatform.WB -> {
                    sdkShareChannels.add(
                        SDKShareChannel(
                            SDKSharePlatform.WB,
                            R.drawable.social_sdk_logo_wb,
                            context.getString(R.string.social_sdk_share_2wb)
                        )
                    )
                }
            }
        }

        return sdkShareChannels
    }

    override fun shareFail(sharePlatform: Int, error: String?) {
        reset()
        onShareFail(sharePlatform, error)
    }

    override fun shareCancel(sharePlatform: Int) {
        reset()
        onShareCancel(sharePlatform)
    }

    override fun shareSuccess(sharePlatform: Int) {
        reset()
        onShareSuccess(sharePlatform)
    }

    override fun startWX(isSucceed: Boolean) {
        if (isSucceed) {

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


    private fun reset() {
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }

    abstract fun onShareSuccess(sharePlatform: Int)
    abstract fun onShareFail(sharePlatform: Int, error: String?)
    abstract fun onShareCancel(sharePlatform: Int)
    abstract fun onStartWXApp(isSucceed: Boolean)
    abstract fun onUnInstallWXApp()
}