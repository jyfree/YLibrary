package com.jy.simple.social

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ShareCompat
import com.jy.baselibrary.utils.BaseUtils
import com.jy.simple.Constants
import com.jy.simple.R
import com.jy.sociallibrary.media.*
import java.io.ByteArrayOutputStream

object ShareUtils {

    fun getWeb(): JYWeb {
        val jyWeb = JYWeb(Constants.URL.SHARE_TARGET_URL)
        jyWeb.title = "分享标题"
        jyWeb.description = "分享内容"
        jyWeb.thumb = JYImage(R.drawable.share_icon)
//        jyWeb.imageUrl = Constants.URL.SHARE_IMAGE_URL
        return jyWeb
    }

    fun getImage(): JYImage {
//        val jyImage = JYImage("/storage/emulated/0/200317190044044.jpg")
//        val jyImage = JYImage(R.drawable.share_icon)
//        val jyImage = JYImage(BitmapFactory.decodeResource(resources, R.drawable.share_icon))
//        val jyImage = JYImage(File("/storage/emulated/0/200317190044044.jpg"))
        val jyImage = JYImage(
            bmp2ByteArray(
                BitmapFactory.decodeResource(
                    BaseUtils.getApp().resources,
                    R.drawable.bg_bar
                )
            )
        )

        jyImage.thumb = JYImage(R.drawable.share_icon)
        return jyImage
    }

    fun getText(): JYText {
        return JYText("分享内容")
    }

    fun getAudio(): JYAudio {
        val jyAudio = JYAudio("http://cdn1.lizhi.fm/audio/2018/12/17/5015505658066976262_hd.mp3")
        jyAudio.title = "音乐标题"
        jyAudio.description = "音乐说明"
        jyAudio.imageUrl = Constants.URL.SHARE_IMAGE_URL
        jyAudio.webUrl =
            "http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1"
        jyAudio.thumb = JYImage(R.drawable.share_icon)
        return jyAudio
    }

    fun getVideo(): JYVideo {

//        val jyVideo = JYVideo(Environment.getExternalStorageDirectory().absolutePath + File.separator + "test.mp4")//微博分享
        val jyVideo = JYVideo("https://cdn.9mitao.com/video/15764805144381434.mp4")//微信分享

        jyVideo.title = "视频标题"
        jyVideo.description = "视频说明"
        jyVideo.thumb = JYImage(R.drawable.share_icon)
        return jyVideo
    }

    fun sysShareAll(activity: Activity) {
        val shareIntent = ShareCompat.IntentBuilder.from(activity)
            .setText("分享内容")
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        if (shareIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(shareIntent)
        }
    }

    fun sysShareSome(activity: Activity) {
        val shareIntent = Intent()
        shareIntent.component =
            ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容")
        if (shareIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(shareIntent)
        }
    }

    private fun bmp2ByteArray(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val datas = baos.toByteArray()
        try {
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return datas
    }
}