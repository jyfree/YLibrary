package com.jy.sociallibrary.wx;

import android.content.Context;
import android.graphics.Bitmap;

import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.media.BaseMediaObject;
import com.jy.sociallibrary.media.JYImage;
import com.jy.sociallibrary.media.JYText;
import com.jy.sociallibrary.media.JYWeb;
import com.jy.sociallibrary.utils.JYImageUtils;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

/**
 * Administrator
 * created at 2016/11/9 17:48
 * TODO:微信分享
 */
public class WXShareManager extends WXChannelManager {


    public WXShareManager(Context context) {
        super(context);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void doShareAll(BaseMediaObject media, boolean isTimelineCb) {

        if (media instanceof JYWeb) {
            JYWeb jyWeb = (JYWeb) media;
            shareWeb(jyWeb.webUrl, jyWeb.title, jyWeb.description, JYImageUtils.getImageBitmap(mContext, jyWeb.thumb), isTimelineCb);
        } else if (media instanceof JYText) {
            JYText jyText = (JYText) media;
            shareText(jyText.content, jyText.content, isTimelineCb);
        } else if (media instanceof JYImage) {
            JYImage jyImage = (JYImage) media;
            if (jyImage.imageType == SDKImageType.URL_IMAGE) {
                SDKLogUtils.i("微信图片分享--图片路径分享");
                shareImage2Path(JYImageUtils.getImagePath(jyImage), JYImageUtils.getImageBitmap(mContext, jyImage.thumb), isTimelineCb);
            } else {
                SDKLogUtils.i("微信图片分享--图片bitmap分享");
                shareImage2Bitmap(JYImageUtils.getImageBitmap(mContext, jyImage), JYImageUtils.getImageBitmap(mContext, jyImage.thumb), isTimelineCb);
            }
        } else {
            //shareAudio(shareInfo.audioUrl, shareInfo.title, shareInfo.summary, shareInfo.thumb, isTimelineCb);
            SDKLogUtils.e("未知分享类型");
        }
    }

    /**
     * 分享网页
     *
     * @param webUrl       网页地址
     * @param title        标题 512长度限制
     * @param description  描述 1024长度限制
     * @param thumb        缩略图 32K
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareWeb(String webUrl, String title, String description, Bitmap thumb, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = webUrl;
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = description;
        if (thumb != null) {
            msg.thumbData = HttpUtils.bmpToByteArray(thumb, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mWxAPI.sendReq(req);

    }

    /**
     * 分享文本
     *
     * @param content      分享内容
     * @param description  分享描述
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareText(String content, String description, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = description;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        //分享方式  朋友圈：WXSceneTimeline，好友会话：WXSceneSession
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        mWxAPI.sendReq(req);
    }

    private void shareImage2Bitmap(Bitmap image, Bitmap thumb, boolean isTimelineCb) {
        if (image == null) {
            SDKLogUtils.d("微信图片分享--图片bitmap分享--bitmap为null");
        }
        WXImageObject imgObj = new WXImageObject(image);
        shareImage(imgObj, thumb, isTimelineCb);
    }

    private void shareImage2Path(String imagePath, Bitmap thumb, boolean isTimelineCb) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = imagePath;
        shareImage(imgObj, thumb, isTimelineCb);

    }

    /**
     * 分享图片
     *
     * @param imgObj       图片实体
     * @param thumb        缩略图
     * @param isTimelineCb 是否分享到朋友圈
     */
    private void shareImage(WXImageObject imgObj, Bitmap thumb, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            if (thumb != null) {
                msg.thumbData = HttpUtils.bmpToByteArray(thumb, true);
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            mWxAPI.sendReq(req);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享音乐
     *
     * @param musicUrl     音乐地址
     * @param title        标题
     * @param description  描述
     * @param thumb        缩略图
     * @param isTimelineCb 是否分享到朋友圈
     */
    private void shareAudio(String musicUrl, String title, String description, Bitmap thumb, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {
            WXMusicObject music = new WXMusicObject();
            music.musicUrl = musicUrl;

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = music;
            msg.title = title;
            msg.description = description;
            if (thumb != null) {
                msg.thumbData = HttpUtils.bmpToByteArray(thumb, true);
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("music");
            req.message = msg;
            req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            mWxAPI.sendReq(req);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
