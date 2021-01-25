package com.jy.sociallibrary.wb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Patterns;

import com.jy.sociallibrary.SDKConfig;
import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.constant.SDKSharePlatform;
import com.jy.sociallibrary.listener.OnSocialSdkShareListener;
import com.jy.sociallibrary.media.BaseMediaObject;
import com.jy.sociallibrary.media.JYAudio;
import com.jy.sociallibrary.media.JYImage;
import com.jy.sociallibrary.media.JYText;
import com.jy.sociallibrary.media.JYVideo;
import com.jy.sociallibrary.media.JYWeb;
import com.jy.sociallibrary.utils.JYImageUtils;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;


/**
 * Administrator
 * created at 2016/11/10 11:17
 * TODO:微博分享
 */
public class WBShareManager implements WbShareCallback {

    private Context mContext;

    /**
     * 微博分享的接口实例
     */
    private WbShareHandler wbShareHandler;

    private boolean hasText;
    private boolean hasImage;
    private boolean hasWebPage;

    private OnSocialSdkShareListener listener;

    public WBShareManager(Context context, OnSocialSdkShareListener listener) {
        this.mContext = context;
        this.listener = listener;
        SDKConfig.initWB();
        initialize();

    }

    /**
     * 初始化微博接口实例 。
     */
    private void initialize() {
        // 创建微博 SDK 接口实例
        wbShareHandler = new WbShareHandler((Activity) mContext);
        // 注册到新浪微博
        wbShareHandler.registerApp();

        setShareAllType();

    }


    /**
     * Activity
     * onCreate处调用
     * onNewIntent处调用
     *
     * @param intent
     */
    public void doResultIntent(Intent intent) {
        wbShareHandler.doResultIntent(intent, this);
    }


    /**
     * 设置是否分享text文本
     *
     * @param hasText
     */
    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    /**
     * 设置是否分享图片
     *
     * @param hasImage
     */
    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    /**
     * 设置是否分享网页
     *
     * @param hasWebPage
     */
    public void setHasWebpage(boolean hasWebPage) {
        this.hasWebPage = hasWebPage;
    }

    /**
     * 设置分享全部类型
     */
    public void setShareAllType() {
        hasText = true;
        hasImage = true;
        hasWebPage = true;
    }

    /**
     * 分享文本消息
     *
     * @param text 消息内容
     */
    public void shareTextMsg(String text) {
        hasText = true;
        hasImage = false;
        hasWebPage = false;
        shareMultiMsg(text, null, null, null, null, null, null, null);
    }

    /**
     * 分享图片消息
     *
     * @param imagePath 图片路径
     */
    public void shareImageMsg(String imagePath) {
        hasText = false;
        hasImage = true;
        hasWebPage = false;
        shareMultiMsg(null, imagePath, null, null, null, null, null, null);
    }

    /**
     * 分享图片消息
     *
     * @param bitmap bitmap对象
     */
    public void shareImageMsg(Bitmap bitmap) {
        hasText = false;
        hasImage = true;
        hasWebPage = false;
        shareMultiMsg(null, null, bitmap, null, null, null, null, null);
    }

    /**
     * 分享网页消息
     *
     * @param title       标题
     * @param description 描述
     * @param defaultText 默认text
     * @param targetUrl   目标链接地址
     * @param thumbImage  缩略图
     */
    public void shareWebpageMsg(String title, String description, String defaultText, String targetUrl, Bitmap thumbImage) {
        hasText = false;
        hasImage = false;
        hasWebPage = true;
        shareMultiMsg(null, null, null, title, description, defaultText, targetUrl, thumbImage);
    }

    /**
     * 分享视频
     *
     * @param videoPath 视频地址
     * @param title     标题
     */
    public void shareVideo(String videoPath, String title) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(title);
        weiboMessage.videoSourceObject = getVideoObject(videoPath);

        wbShareHandler.shareMessage(weiboMessage, false);
    }

    /**
     * 多种分享方式结合
     * 分享类型：SHARE_ALL_IN_ONE
     *
     * @param text        文本消息分享
     * @param imagePath   图片消息分享
     * @param title       标题（网页消息分享）
     * @param description 描述（网页消息分享）
     * @param defaultText 默认text（网页消息分享）
     * @param targetUrl   目标链接地址（网页消息分享）
     * @param thumbImage  缩略图（网页消息分享）
     */
    public void shareMultiMsg(String text, String imagePath, Bitmap imageBitmap, String title, String description, String defaultText, String targetUrl, Bitmap thumbImage) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj(text);
        }
        if (hasImage) {
            if (imagePath == null && imageBitmap == null) {
                SDKLogUtils.e("微博分享--image is null");
            } else if (imageBitmap != null) {
                weiboMessage.imageObject = getImageObj(imageBitmap);
            } else {
                weiboMessage.imageObject = getImageObj(imagePath);
            }

        }
        if (hasWebPage) {
            weiboMessage.mediaObject = getWebpageObj(title, description, defaultText, targetUrl, thumbImage);
        }
        wbShareHandler.shareMessage(weiboMessage, false);

    }

    public void doShareAll(BaseMediaObject media) {

        if (media instanceof JYWeb) {
            hasText = false;
            hasImage = true;
            hasWebPage = true;

            JYWeb jyWeb = (JYWeb) media;
            Bitmap bitmap = JYImageUtils.getImageBitmap(mContext, jyWeb.thumb);
            shareMultiMsg(null, null, bitmap, jyWeb.title, jyWeb.description, jyWeb.description, jyWeb.webUrl, bitmap);
        } else if (media instanceof JYImage) {
            JYImage jyImage = (JYImage) media;
            if (jyImage.imageType == SDKImageType.URL_IMAGE) {
                shareImageMsg(JYImageUtils.getImagePath(jyImage));
            } else {
                shareImageMsg(JYImageUtils.getImageBitmap(mContext, jyImage));
            }
        } else if (media instanceof JYText) {
            JYText jyText = (JYText) media;
            shareTextMsg(jyText.content);
        } else if (media instanceof JYVideo) {
            JYVideo jyVideo = (JYVideo) media;
            if (Patterns.WEB_URL.matcher(jyVideo.videoUrl).matches()) {
                listener.shareFail(SDKSharePlatform.WB, "微博只支持本地视频分享");
                return;
            }
            SDKLogUtils.i("微博分享视频，需要稍等片刻才能在微博上看到，因为需要上传文件");
            shareVideo(jyVideo.videoUrl, jyVideo.title);
        } else if (media instanceof JYAudio) {
            listener.shareFail(SDKSharePlatform.WB, "微博不支持音频分享");
        } else {
            listener.shareFail(SDKSharePlatform.WB, "未知分享类型");
        }
    }

    /**
     * 创建文本消息对象。
     *
     * @param text 消息内容
     * @return 文本消息对象
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @param imagePath 图片路径
     * @return 图片消息对象
     */
    private ImageObject getImageObj(String imagePath) {
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath = imagePath;
        return imageObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @param bitmap 图片
     * @return 图片消息对象
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @param title       标题
     * @param description 描述
     * @param targetUrl   目标链接地址
     * @param thumbImage  缩略图  （注意：最终压缩过的缩略图大小不得超过 32kb)
     * @return 多媒体（网页）消息对象
     */
    private WebpageObject getWebpageObj(String title, String description, String defaultText, String targetUrl, Bitmap thumbImage) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
        mediaObject.setThumbImage(thumbImage);
        mediaObject.actionUrl = targetUrl;
        mediaObject.defaultText = defaultText;
        return mediaObject;
    }

    /**
     * 分享视频
     *
     * @param videoPath
     * @return
     */
    private VideoSourceObject getVideoObject(String videoPath) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = Uri.fromFile(new File(videoPath));
        return videoSourceObject;
    }

    @Override
    public void onWbShareSuccess() {
        SDKLogUtils.i("微博分享授权--成功");
        listener.shareSuccess(SDKSharePlatform.WB);
    }

    @Override
    public void onWbShareCancel() {
        SDKLogUtils.i("微博分享授权--取消");
        listener.shareCancel(SDKSharePlatform.WB);
    }

    @Override
    public void onWbShareFail() {
        SDKLogUtils.e("微博分享授权--失败");
        listener.shareFail(SDKSharePlatform.WB, "分享失败");
    }
}
