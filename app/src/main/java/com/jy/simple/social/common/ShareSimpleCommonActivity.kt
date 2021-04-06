package com.jy.simple.social.common

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.simple.social.ShareUtils
import com.jy.sociallibrary.constant.SDKSharePlatform
import com.jy.sociallibrary.ext.share.SDKShareCommonActivity

class ShareSimpleCommonActivity : SDKShareCommonActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, ShareSimpleCommonActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_share_activity)
    }

    fun onClickShare(view: View) {
        when (view.id) {
            R.id.share_2qq -> toShare(SDKSharePlatform.QQ_FRIENDS)
            R.id.share_2qzone -> toShare(SDKSharePlatform.QQ_QZONE)
            R.id.share_2wechat -> toShare(SDKSharePlatform.WX_FRIENDS)
            R.id.share_2wechatmoments -> toShare(SDKSharePlatform.WX_CB)
            R.id.share_2wb -> toShare(SDKSharePlatform.WB)
            R.id.share_all_web -> requestShare(ShareUtils.getWeb())
            R.id.share_all_image -> requestShare(ShareUtils.getImage())
            R.id.share_all_text -> requestShare(ShareUtils.getText())
            R.id.share_all_audio -> requestShare(ShareUtils.getAudio())
            R.id.share_all_video -> {
                requestShare(
                    ShareUtils.getVideo(), arrayOf(
                        SDKSharePlatform.WX_FRIENDS,
                        SDKSharePlatform.WX_CB,
                        SDKSharePlatform.WB
                    )
                )
            }
            R.id.share_sys_all -> ShareUtils.sysShareAll(this)
            R.id.share_sys_some -> ShareUtils.sysShareSome(this)
        }
    }

    private fun toShare(sharePlatform: Int) {
        requestShare(ShareUtils.getWeb(), sharePlatform)

    }

    override fun onShareSuccess(sharePlatform: Int) {
        YLogUtils.i("分享成功--平台：", sharePlatform)
    }

    override fun onShareFail(sharePlatform: Int, error: String?) {
        YLogUtils.e("分享失败--平台：", sharePlatform, "error", error)
    }

    override fun onShareCancel(sharePlatform: Int) {
        YLogUtils.i("取消分享--平台：", sharePlatform)
    }

    override fun onStartWXApp(isSucceed: Boolean) {
        YLogUtils.e("启动微信成功？", isSucceed)
    }

    override fun onUnInstallWXApp() {
        YLogUtils.e("未安装微信")
    }
}