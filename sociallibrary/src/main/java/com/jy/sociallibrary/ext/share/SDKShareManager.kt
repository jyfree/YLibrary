package com.jy.sociallibrary.ext.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jy.sociallibrary.R
import com.jy.sociallibrary.manager.SDKShare
import com.jy.sociallibrary.bean.SDKShareChannel
import com.jy.sociallibrary.bean.ShareInfo
import com.jy.sociallibrary.constant.SDKShareType
import com.jy.sociallibrary.dialog.SDKShareDialog
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.listener.OnSocialSdkShareListener
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener


/**

 * @Author Administrator
 * @Date 2019/12/27-10:07
 * @TODO
 */
class SDKShareManager {

    private var sdkShareChannels: ArrayList<SDKShareChannel>? = null
    private var sdkShare: SDKShare? = null
    private var shareListener: OnSocialSdkShareListener? = null
    private var wxListener: WXListener? = null
    private val SHARE_TYPE = "shareType"
    private val SHARE_INFO = "shareInfo"
    private var shareType: Int = SDKShareType.TYPE_WX_CB

    fun setShareListener(shareListener: OnSocialSdkShareListener): SDKShareManager {
        this.shareListener = shareListener
        return this
    }

    fun setWXListener(wxListener: WXListener): SDKShareManager {
        this.wxListener = wxListener
        return this
    }

    fun requestShare(context: Context, shareType: Int, shareInfo: ShareInfo) {
        val intent = Intent(context, SDKShareActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SHARE_TYPE, shareType)
        intent.putExtra(SHARE_INFO, shareInfo)
        context.startActivity(intent)
    }

    fun requestShare(context: Context, shareInfo: ShareInfo) {
        showShareDialog(context, shareInfo)
    }

    fun behavior(activity: Activity, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            initShare(activity)
        }
    }

    private fun initShare(activity: Activity) {
        sdkShare = SDKShare(
            activity,
            object : OnSocialSdkShareListener {
                override fun shareSuccess(type: Int) {
                    onDestroy(activity)
                    shareListener?.shareSuccess(type)
                }

                override fun shareFail(type: Int, error: String?) {
                    onDestroy(activity)
                    shareListener?.shareFail(type, error)
                }

                override fun shareCancel(type: Int) {
                    onDestroy(activity)
                    shareListener?.shareCancel(type)
                }
            })
        sdkShare?.setWXListener {
            onDestroy(activity)
            wxListener?.installWXAPP()
        }
    }


    private fun prepareShareData(context: Context) {
        sdkShareChannels = ArrayList()
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKShareType.TYPE_WX_FRIENDS,
                R.drawable.social_sdk_logo_wechat,
                context.getString(R.string.social_sdk_share_2wechat)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKShareType.TYPE_QQ_FRIENDS,
                R.drawable.social_sdk_logo_qq,
                context.getString(R.string.social_sdk_share_2qq)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKShareType.TYPE_WX_CB,
                R.drawable.social_sdk_logo_wechatmoments,
                context.getString(R.string.social_sdk_share_2wechatmoments)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKShareType.TYPE_QQ_QZONE,
                R.drawable.social_sdk_logo_qzone,
                context.getString(R.string.social_sdk_share_2qzone)
            )
        )
        sdkShareChannels?.add(
            SDKShareChannel(
                SDKShareType.TYPE_WB,
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

        val shareInfo = intent.getParcelableExtra<ShareInfo>(SHARE_INFO)
        val shareType = intent.getIntExtra(SHARE_TYPE, 0)
        onShare(shareType, shareInfo)

    }


    private fun showShareDialog(context: Context, shareInfo: ShareInfo) {
        if (sdkShareChannels == null) {
            prepareShareData(context)
        }
        val sdkShareDialog = SDKShareDialog(
            context,
            R.style.social_Theme_dialog,
            sdkShareChannels,
            SDKShareDialog.OnSDKShareListener {
                requestShare(context, it.id, shareInfo)
            })
        sdkShareDialog.initSystemUI()
        sdkShareDialog.show()
    }

    private fun onShare(type: Int, shareInfo: ShareInfo) {

        shareType = type
        sdkShare?.share(type, shareInfo)
    }

    fun doResultIntent(data: Intent?) {
        sdkShare?.doResultIntent(data)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        sdkShare?.result2Activity(requestCode, resultCode, data)
    }


    fun onResultToWXShareSuccess(activity: Activity) {
        onDestroy(activity)
        shareListener?.shareSuccess(shareType)
    }

    fun onResultToWXShareCancel(activity: Activity) {
        onDestroy(activity)
        shareListener?.shareCancel(shareType)
    }

    fun onResultToWXShareFail(activity: Activity, errCode: Int) {
        onDestroy(activity)
        shareListener?.shareFail(shareType, "错误码：$errCode")
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