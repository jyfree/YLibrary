package com.jy.sociallibrary.ext.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

 * @Author Administrator
 * @Date 2019/12/27-10:07
 * @TODO
 */
class SDKShareManager {

    private var shareHelper: ShareHelper? = null
    private var shareListener: OnSocialSdkShareListener? = null
    private var wxListener: WXListener? = null
    private val SHARE_PLATFORM = "sharePlatform"//分享平台
    private val SHARE_MEDIA = "shareMedia"//分享媒体
    private var sharePlatform: Int = SDKSharePlatform.WX_CB

    fun setShareListener(shareListener: OnSocialSdkShareListener): SDKShareManager {
        this.shareListener = shareListener
        return this
    }

    fun setWXListener(wxListener: WXListener): SDKShareManager {
        this.wxListener = wxListener
        return this
    }

    /**
     * 必需注册LiveData，用于微信回调
     * todo 注意：从任务管理器切换回app是无法收到微信回调的
     */
    fun registerObserve(owner: LifecycleOwner): SDKShareManager {
        StatusLiveData.getInstance().observe(owner, Observer<StatusBean?> {
            it?.let {
                when (it.status) {
                    SDKConstants.ShareStatus.WX_SHARE_SUCCESS -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信分享--成功")
                        onResultToWXShareSuccess(null)
                    }
                    SDKConstants.ShareStatus.WX_SHARE_FAIL -> {
                        SDKLogUtils.e("接收到MutableLiveData--微信分享--失败--errCode", it.errCode)
                        onResultToWXShareFail(null, it.errCode)
                    }
                    SDKConstants.ShareStatus.WX_SHARE_CANCEL -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信分享--取消")
                        onResultToWXShareCancel(null)
                    }
                }
            }
        })
        return this
    }

    fun requestShare(context: Context, media: BaseMediaObject, sharePlatform: Int) {
        val intent = Intent(context, SDKShareActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SHARE_PLATFORM, sharePlatform)
        intent.putExtra(SHARE_MEDIA, media)
        context.startActivity(intent)
    }

    fun requestShare(context: Context, media: BaseMediaObject) {
        val platformArray = arrayOf(
            SDKSharePlatform.WX_FRIENDS,
            SDKSharePlatform.WX_CB,
            SDKSharePlatform.QQ_FRIENDS,
            SDKSharePlatform.QQ_QZONE,
            SDKSharePlatform.WB
        )
        showShareDialog(context, media, platformArray)
    }

    fun requestShare(context: Context, media: BaseMediaObject, platformArray: Array<Int>) {
        showShareDialog(context, media, platformArray)
    }

    fun behavior(activity: Activity, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            initShare(activity)
        }
    }

    private fun initShare(activity: Activity) {
        shareHelper = ShareHelper(
            activity,
            object : OnSocialSdkShareListener {
                override fun shareSuccess(sharePlatform: Int) {
                    onDestroy(activity)
                    shareListener?.shareSuccess(sharePlatform)
                }

                override fun shareFail(sharePlatform: Int, error: String?) {
                    onDestroy(activity)
                    shareListener?.shareFail(sharePlatform, error)
                }

                override fun shareCancel(sharePlatform: Int) {
                    onDestroy(activity)
                    shareListener?.shareCancel(sharePlatform)
                }
            })
        shareHelper?.setWXListener(object : WXListener {
            override fun startWX(isSucceed: Boolean) {
                if (isSucceed) {
                    activity.finish()
                } else {
                    onDestroy(activity)
                }
                wxListener?.startWX(isSucceed)
            }

            override fun installWXAPP() {
                SDKLogUtils.e("未安装微信")
                onDestroy(activity)
                wxListener?.installWXAPP()
            }

        })
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


    fun checkShare(activity: Activity, intent: Intent?) {

        if (intent == null) {
            SDKLogUtils.e("checkShare intent is null")
            onDestroy(activity)
            return
        }
        if (intent.extras == null) {
            SDKLogUtils.e("checkShare extras is null")
            onDestroy(activity)
            return
        }

        val mediaObject = intent.getParcelableExtra<BaseMediaObject>(SHARE_MEDIA)
        val sharePlatform = intent.getIntExtra(SHARE_PLATFORM, 0)
        if (mediaObject == null) {
            onDestroy(activity)
            return
        }
        onShare(sharePlatform, mediaObject)

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
                requestShare(context, media, it.platform)
            })
        sdkShareDialog.initSystemUI()
        sdkShareDialog.show()
    }

    private fun onShare(platform: Int, media: BaseMediaObject) {

        sharePlatform = platform
        shareHelper?.share(platform, media)
    }

    fun doResultIntent(data: Intent?) {
        shareHelper?.doResultIntent(data)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        shareHelper?.result2Activity(requestCode, resultCode, data)
    }


    private fun onResultToWXShareSuccess(activity: Activity?) {
        onDestroy(activity)
        shareListener?.shareSuccess(sharePlatform)
    }

    private fun onResultToWXShareCancel(activity: Activity?) {
        onDestroy(activity)
        shareListener?.shareCancel(sharePlatform)
    }

    private fun onResultToWXShareFail(activity: Activity?, errCode: Int) {
        onDestroy(activity)
        shareListener?.shareFail(sharePlatform, "错误码：$errCode")
    }


    /**
     * 摧毁本库的 SDKShareActivity
     */
    private fun onDestroy(activity: Activity?) {
        shareHelper = null
        activity?.finish()
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }
}