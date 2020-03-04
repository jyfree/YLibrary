package com.jy.simple.social


import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.social.share.ExtShare
import com.jy.commonlibrary.social.share.SDKShareManager
import com.jy.simple.Constants
import com.jy.simple.R
import com.jy.sociallibrary.bean.ShareInfo
import com.jy.sociallibrary.constant.SDKShareType
import com.jy.sociallibrary.listener.OnSocialSdkShareListener
import com.jy.sociallibrary.wx.WXListener


/**

 * @Author Administrator
 * @Date 2019/11/12-14:40
 * @TODO
 */
class ShareSimpleActivity : Activity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, ShareSimpleActivity::class.java)
        }
    }

    private var sdkShareManager: SDKShareManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_share_activity)
    }


    fun onClickShare(view: View) {
        when (view.id) {
            R.id.share_2qq -> toShare(SDKShareType.TYPE_QQ_FRIENDS)
            R.id.share_2qzone -> toShare(SDKShareType.TYPE_QQ_QZONE)
            R.id.share_2wechat -> toShare(SDKShareType.TYPE_WX_FRIENDS)
            R.id.share_2wechatmoments -> toShare(SDKShareType.TYPE_WX_CB)
            R.id.share_2wb -> toShare(SDKShareType.TYPE_WB)
            R.id.share_all -> showShareDialog()
        }
    }

    private fun toShare(shareType: Int) {
        getSdkShareManager().requestShare(this, shareType, getShareInfo())
    }

    private fun showShareDialog() {
        getSdkShareManager().requestShare(this, getShareInfo())
    }

    private fun getSdkShareManager(): SDKShareManager {
        if (sdkShareManager == null) {
            sdkShareManager = ExtShare.instance.sdkShareManager.setShareListener(object : OnSocialSdkShareListener {
                override fun shareSuccess(type: Int) {
                    YLogUtils.i("分享成功--类型：", type)
                }

                override fun shareFail(type: Int, error: String?) {
                    YLogUtils.e("分享失败--类型：", type, "error", error)
                }

                override fun shareCancel(type: Int) {
                    YLogUtils.i("取消分享--类型：", type)
                }

            }).setWXListener(WXListener {
                YLogUtils.e("未安装微信")
            })
        }
        return sdkShareManager!!
    }

    private fun getShareInfo(): ShareInfo {
        val shareInfoVo = ShareInfo()
        shareInfoVo.appName = getString(R.string.app_name)
        //图片不能超过32K
        shareInfoVo.bitmap = BitmapFactory.decodeResource(resources, R.drawable.share_icon)
        shareInfoVo.imageUrl = Constants.URL.SHARE_IMAGE_URL//分享imageUrl
        shareInfoVo.summary = "撩起来"
        shareInfoVo.title = getString(R.string.app_name)
        shareInfoVo.targetUrl = Constants.URL.SHARE_TARGET_URL//分享目标地址

        return shareInfoVo
    }

}