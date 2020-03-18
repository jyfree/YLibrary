package com.jy.simple.social


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.Constants
import com.jy.simple.R
import com.jy.sociallibrary.constant.SDKSharePlatform
import com.jy.sociallibrary.ext.share.SDKShare
import com.jy.sociallibrary.ext.share.SDKShareManager
import com.jy.sociallibrary.listener.OnSocialSdkShareListener
import com.jy.sociallibrary.media.*
import com.jy.sociallibrary.wx.WXListener
import java.io.File


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
            R.id.share_2qq -> toShare(SDKSharePlatform.QQ_FRIENDS)
            R.id.share_2qzone -> toShare(SDKSharePlatform.QQ_QZONE)
            R.id.share_2wechat -> toShare(SDKSharePlatform.WX_FRIENDS)
            R.id.share_2wechatmoments -> toShare(SDKSharePlatform.WX_CB)
            R.id.share_2wb -> toShare(SDKSharePlatform.WB)
            R.id.share_all_web -> getSdkShareManager().requestShare(this, getWeb())
            R.id.share_all_image -> getSdkShareManager().requestShare(this, getImage())
            R.id.share_all_text -> getSdkShareManager().requestShare(this, getText())
            R.id.share_all_audio -> getSdkShareManager().requestShare(this, getAudio())
            R.id.share_all_video -> getSdkShareManager().requestShare(this, getVideo())
        }
    }

    private fun toShare(sharePlatform: Int) {
        getSdkShareManager().requestShare(this, sharePlatform, getWeb())

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

            }).setWXListener(WXListener {
                YLogUtils.e("未安装微信")
            })
        }
        return sdkShareManager!!
    }

    private fun getWeb(): JYWeb {
        val jyWeb = JYWeb(Constants.URL.SHARE_TARGET_URL)
        jyWeb.title = "分享标题"
        jyWeb.description = "分享内容"
        jyWeb.thumb = JYImage(R.drawable.share_icon)
        jyWeb.imageUrl = Constants.URL.SHARE_IMAGE_URL
        return jyWeb
    }

    private fun getImage(): JYImage {
        val jyImage = JYImage("/storage/emulated/0/200317190044044.jpg")
//        val jyImage = JYImage(R.drawable.share_icon)
//        val jyImage = JYImage(BitmapFactory.decodeResource(resources, R.drawable.share_icon))
//        val jyImage = JYImage(File("/storage/emulated/0/200317190044044.jpg"))

        jyImage.thumb = JYImage(R.drawable.share_icon)
        return jyImage
    }

    private fun getText(): JYText {
        return JYText("分享内容")
    }

    private fun getAudio(): JYAudio {
        val jyAudio = JYAudio("http://cdn1.lizhi.fm/audio/2018/12/17/5015505658066976262_hd.mp3")
        jyAudio.title = "音乐标题"
        jyAudio.description = "音乐说明"
        jyAudio.imageUrl = Constants.URL.SHARE_IMAGE_URL
        jyAudio.webUrl =
            "http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1"
        jyAudio.thumb = JYImage(R.drawable.share_icon)
        return jyAudio
    }

    private fun getVideo(): JYVideo {

//        val jyVideo = JYVideo(Environment.getExternalStorageDirectory().absolutePath + File.separator + "test.mp4")//微博分享
        val jyVideo = JYVideo("https://cdn.9mitao.com/video/15764805144381434.mp4")//微信分享

        jyVideo.title = "视频标题"
        jyVideo.description = "视频说明"
        jyVideo.thumb = JYImage(R.drawable.share_icon)
        return jyVideo
    }
}