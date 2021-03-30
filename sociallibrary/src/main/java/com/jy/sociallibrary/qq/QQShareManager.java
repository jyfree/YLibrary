package com.jy.sociallibrary.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jy.sociallibrary.SDKThreadManager;
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
import com.jy.sociallibrary.utils.SDKAppUtils;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Administrator
 * created at 2016/11/10 10:06
 * TODO:QQ分享
 * 若分享失败，查看文档：https://wiki.connect.qq.com/分享功能存储权限适配
 */
public class QQShareManager extends QQChannelManager {


    private OnSocialSdkShareListener qqShareListener;
    private int sharePlatform = SDKSharePlatform.QQ_FRIENDS;

    public QQShareManager(Context context) {
        super(context);
    }


    public void setQqShareListener(OnSocialSdkShareListener qqShareListener) {
        this.qqShareListener = qqShareListener;
    }

    /**
     * QQ默认分享(可分享给好友|分享到QQ空间)
     * <p>
     * title           标题（最大长度45）
     * summary         内容（最大长度60）
     * targetUrl       目标链接URL
     * imageUrl        图片URL
     * appName         程序名称
     * isAutoOpenQZone 是否分享到Qzone
     */
    public void doShareAll(BaseMediaObject media, boolean isAutoOpenQZone) {
        //分享平台：QQ好友，QQ空间
        int mExtarFlag;
        if (isAutoOpenQZone) {
            sharePlatform = SDKSharePlatform.QQ_QZONE;
            mExtarFlag = QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
        } else {
            sharePlatform = SDKSharePlatform.QQ_FRIENDS;
            mExtarFlag = QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
        }

        final Bundle params = new Bundle();

        //分享类型
        int shareType;
        if (media instanceof JYWeb) {
            shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;

            JYWeb jyWeb = (JYWeb) media;
            params.putString(QQShare.SHARE_TO_QQ_TITLE, jyWeb.title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, jyWeb.webUrl);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, jyWeb.description);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, jyWeb.imageUrl);

        } else if (media instanceof JYImage) {
            shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;

            JYImage jyImage = (JYImage) media;
            if (jyImage.imageType == SDKImageType.RES_IMAGE
                    || jyImage.imageType == SDKImageType.BITMAP_IMAGE
                    || jyImage.imageType == SDKImageType.BYTE_ARRAY) {
                qqShareListener.shareFail(sharePlatform, "QQ只支持本地纯图片分享");
                return;
            }
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, JYImageUtils.getImagePath(jyImage));
        } else if (media instanceof JYText) {
            qqShareListener.shareFail(sharePlatform, "QQ不支持纯文本分享");
            return;
        } else if (media instanceof JYAudio) {
            shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO;

            JYAudio jyAudio = (JYAudio) media;
            params.putString(QQShare.SHARE_TO_QQ_TITLE, jyAudio.title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, jyAudio.webUrl);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, jyAudio.description);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, jyAudio.imageUrl);
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, jyAudio.audioUrl);

        } else if (media instanceof JYVideo) {
            qqShareListener.shareFail(sharePlatform, "QQ不支持视频分享");
            return;
        } else {
            qqShareListener.shareFail(sharePlatform, "未知分享类型");
            return;
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, SDKAppUtils.getAppName(mContext));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);

        doShareToQQ(params);
    }

    /**
     * 分享到QQ空间(单个imageUrl)
     *
     * @param title     标题
     * @param summary   内容
     * @param targetUrl 目标链接URL
     * @param imageUrl  图片地址（可为null|空）
     */
    public void doShare2QZone(String title, String summary, String targetUrl, String imageUrl) {

        ArrayList<String> imageUrls = new ArrayList<>();
        if (null != imageUrl && !"".equals(imageUrl.trim())) {
            imageUrls.add(imageUrl);
        }
        doShare2QZone(title, summary, targetUrl, imageUrls);


    }

    /**
     * 分享到QQ空间(多个imageUrl)
     *
     * @param title     标题
     * @param summary   内容
     * @param targetUrl 目标链接URL
     * @param imageUrls 图片URL集合（可分享多张图片）
     */
    public void doShare2QZone(String title, String summary, String targetUrl, ArrayList<String> imageUrls) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        // 支持传多个imageUrl
        if (null != imageUrls && imageUrls.size() > 0) {
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        }
        sharePlatform = SDKSharePlatform.QQ_QZONE;
        doShareToQQ(params);
    }

    /**
     * QQ分享要在主线程触发
     *
     * @param params 分享类型给内容参数
     */
    public void doShareToQQ(final Bundle params) {

        SDKThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != mTencent && mContext != null) {
                    mTencent.shareToQQ((Activity) mContext, params, shareListener);
                }
            }
        });
    }

    /**
     * 分享回调监听器
     */
    private IUiListener shareListener = new BaseUIListener() {
        @Override
        protected void doComplete(JSONObject values) {
            SDKLogUtils.i("QQ分享授权--成功--AuthorSwitch_SDK:", values);
            qqShareListener.shareSuccess(sharePlatform);
        }

        @Override
        public void onError(UiError e) {
            super.onError(e);
            SDKLogUtils.e("QQ分享授权--失败--errorCode", e.errorCode, "errorMessage", e.errorMessage);
            qqShareListener.shareFail(sharePlatform, e.errorMessage);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            SDKLogUtils.i("QQ分享授权--取消");
            qqShareListener.shareCancel(sharePlatform);
        }
    };

    @Override
    public void result2Activity(int requestCode, int resultCode, Intent data) {
        //qq分享&Qzone分享回调
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }

    }

}
