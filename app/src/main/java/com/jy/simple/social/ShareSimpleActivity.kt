package com.jy.simple.social


import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.sociallibrary.constant.SDKSharePlatform
import com.jy.sociallibrary.ext.share.SDKShare
import com.jy.sociallibrary.ext.share.SDKShareManager
import com.jy.sociallibrary.listener.OnSocialSdkShareListener
import com.jy.sociallibrary.wx.WXListener


/**

 * @Author Administrator
 * @Date 2019/11/12-14:40
 * @TODO
 */
class ShareSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, ShareSimpleActivity::class.java)
        }
    }

    private var sdkShareManager: SDKShareManager? = null

    override fun initLayoutID(): Int = R.layout.simple_share_activity

    override fun initUI(savedInstanceState: Bundle?) {

    }

    fun onClickShare(view: View) {
        when (view.id) {
            R.id.share_2qq -> toShare(SDKSharePlatform.QQ_FRIENDS)
            R.id.share_2qzone -> toShare(SDKSharePlatform.QQ_QZONE)
            R.id.share_2wechat -> toShare(SDKSharePlatform.WX_FRIENDS)
            R.id.share_2wechatmoments -> toShare(SDKSharePlatform.WX_CB)
            R.id.share_2wb -> toShare(SDKSharePlatform.WB)
            R.id.share_all_web -> getSdkShareManager().requestShare(this, ShareUtils.getWeb())
            R.id.share_all_image -> getSdkShareManager().requestShare(this, ShareUtils.getImage())
            R.id.share_all_text -> getSdkShareManager().requestShare(this, ShareUtils.getText())
            R.id.share_all_audio -> getSdkShareManager().requestShare(this, ShareUtils.getAudio())
            R.id.share_all_video -> {
                getSdkShareManager().requestShare(
                    this, ShareUtils.getVideo(), arrayOf(
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
        getSdkShareManager().requestShare(this, ShareUtils.getWeb(), sharePlatform)

    }

    private fun getSdkShareManager(): SDKShareManager {
        if (sdkShareManager == null) {
            sdkShareManager = SDKShare.instance.sdkShareManager.setShareListener(object :
                OnSocialSdkShareListener {
                override fun shareSuccess(sharePlatform: Int) {
                    YLogUtils.i("分享成功--平台：", sharePlatform)
                }

                override fun shareFail(sharePlatform: Int, error: String?) {
                    YLogUtils.e("分享失败--平台：", sharePlatform, "error", error)
                }

                override fun shareCancel(sharePlatform: Int) {
                    YLogUtils.i("取消分享--平台：", sharePlatform)
                }

            }).setWXListener(object : WXListener {
                override fun startWX(isSucceed: Boolean) {
                    YLogUtils.e("启动微信成功？", isSucceed)
                }

                override fun installWXAPP() {
                    YLogUtils.e("未安装微信")
                }

            }).registerObserve(this)
        }
        return sdkShareManager!!
    }
}