package com.jy.sociallibrary.ext.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jy.sociallibrary.R
import com.jy.sociallibrary.bean.SDKShareChannel
import com.jy.sociallibrary.constant.SDKSharePlatform
import com.jy.sociallibrary.dialog.SDKShareDialog
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

    private var sdkShareChannels: ArrayList<SDKShareChannel>? = null
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

    fun requestShare(context: Context, sharePlatform: Int, media: BaseMediaObject) {
        val intent = Intent(context, SDKShareActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SHARE_PLATFORM, sharePlatform)
        intent.putExtra(SHARE_MEDIA, media)
        context.startActivity(intent)
    }

    fun requestShare(context: Context, media: BaseMediaObject) {
        showShareDialog(context, media)
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
        shareHelper?.setWXListener {
            onDestroy(activity)
            wxListener?.installWXAPP()
        }
    }


    private fun prepareShareData(context: Context) {
        sdkShareChannels = ArrayList()
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKSharePlatform.WX_FRIENDS,
                R.drawable.social_sdk_logo_wechat,
                context.getString(R.string.social_sdk_share_2wechat)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKSharePlatform.QQ_FRIENDS,
                R.drawable.social_sdk_logo_qq,
                context.getString(R.string.social_sdk_share_2qq)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKSharePlatform.WX_CB,
                R.drawable.social_sdk_logo_wechatmoments,
                context.getString(R.string.social_sdk_share_2wechatmoments)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKSharePlatform.QQ_QZONE,
                R.drawable.social_sdk_logo_qzone,
                context.getString(R.string.social_sdk_share_2qzone)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKSharePlatform.WB,
                R.drawable.social_sdk_logo_wb,
                context.getString(R.string.social_sdk_share_2wb)
            )
        )
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


    private fun showShareDialog(context: Context, media: BaseMediaObject) {
        if (sdkShareChannels == null) {
            prepareShareData(context)
        }
        val sdkShareDialog = SDKShareDialog(
            context,
            R.style.social_Theme_dialog,
            sdkShareChannels,
            SDKShareDialog.OnSDKShareListener {
                requestShare(context, it.id, media)
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


    fun onResultToWXShareSuccess(activity: Activity) {
        onDestroy(activity)
        shareListener?.shareSuccess(sharePlatform)
    }

    fun onResultToWXShareCancel(activity: Activity) {
        onDestroy(activity)
        shareListener?.shareCancel(sharePlatform)
    }

    fun onResultToWXShareFail(activity: Activity, errCode: Int) {
        onDestroy(activity)
        shareListener?.shareFail(sharePlatform, "错误码：$errCode")
    }


    /**
     * 摧毁本库的 SDKShareActivity
     */
    private fun onDestroy(activity: Activity?) {
        activity?.finish()
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }
}