package com.jy.sociallibrary.wx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.net.URL;

/**
 * Administrator
 * created at 2016/11/9 17:48
 * TODO:微信分享
 */
public class WXShareManager extends WXChannelManager {

    private static final int THUMB_SIZE = 150;

    public WXShareManager(Context context) {
        super(context);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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

    /**
     * 分享本地图片
     *
     * @param path         图片路径
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareImage2Path(String path, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(path);
            Bitmap bmp = BitmapFactory.decodeFile(path);
            shareImage(imgObj, bmp, isTimelineCb);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享图片URL
     *
     * @param url          图片URL
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareImage2Url(String url, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {
            WXImageObject imgObj = new WXImageObject();
            imgObj.imagePath = url;
            Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
            shareImage(imgObj, bmp, isTimelineCb);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享图片二进制数据
     *
     * @param bmp          bitmap对象
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareImage2Bitmap(Bitmap bmp, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {
            WXImageObject imgObj = new WXImageObject(bmp);
            shareImage(imgObj, bmp, isTimelineCb);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(WXImageObject imgObj, Bitmap bmp, boolean isTimelineCb) {
        if (!checkInstallWX())
            return;
        try {

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.thumbData = HttpUtils.bmpToByteArray(thumbBmp, true);

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
     * 分享网页
     *
     * @param webUrl       网页地址
     * @param title        标题
     * @param description  描述
     * @param imageUrl     图片URL
     * @param isTimelineCb 是否分享到朋友圈
     */
    public void shareWeb(String webUrl, String title, String description, String imageUrl, boolean isTimelineCb) {

        try {

            Bitmap bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            shareWeb(webUrl, title, description, thumbBmp, isTimelineCb);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        msg.thumbData = HttpUtils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mWxAPI.sendReq(req);

    }
}
