package com.jy.sociallibrary.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jy.sociallibrary.SDKThreadManager;
import com.jy.sociallibrary.constant.SDKShareType;
import com.jy.sociallibrary.listener.OnSocialSdkShareListener;
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
 */
public class QQShareManager extends QQChannelManager {


    private OnSocialSdkShareListener qqShareListener;
    private int shareType = SDKShareType.TYPE_QQ_FRIENDS;

    public QQShareManager(Context context) {
        super(context);
    }


    public void setQqShareListener(OnSocialSdkShareListener qqShareListener) {
        this.qqShareListener = qqShareListener;
    }

    /**
     * QQ默认分享(可分享给好友|分享到QQ空间)
     *
     * @param title           标题（最大长度45）
     * @param summary         内容（最大长度60）
     * @param targetUrl       目标链接URL
     * @param imageUrl        图片URL
     * @param appName         程序名称
     * @param isAutoOpenQZone 是否分享到Qzone
     */
    public void doShareAll(String title, String summary, String targetUrl, String imageUrl, String appName, boolean isAutoOpenQZone) {
        final Bundle params = new Bundle();

        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        int mExtarFlag;
        if (isAutoOpenQZone) {
            shareType = SDKShareType.TYPE_QQ_QZONE;
            mExtarFlag = QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
        } else {
            shareType = SDKShareType.TYPE_QQ_FRIENDS;
            mExtarFlag = QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
        }
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
        shareType = SDKShareType.TYPE_QQ_QZONE;
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
            qqShareListener.shareSuccess(shareType);
        }

        @Override
        public void onError(UiError e) {
            super.onError(e);
            SDKLogUtils.e("QQ分享授权--失败--errorCode", e.errorCode, "errorMessage", e.errorMessage);
            qqShareListener.shareFail(shareType, e.errorMessage);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            SDKLogUtils.i("QQ分享授权--取消");
            qqShareListener.shareCancel(shareType);
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
