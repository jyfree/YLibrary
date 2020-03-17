package com.jy.sociallibrary.helper;

import android.content.Context;
import android.content.Intent;

import com.jy.sociallibrary.constant.SDKSharePlatform;
import com.jy.sociallibrary.listener.OnSocialSdkShareListener;
import com.jy.sociallibrary.media.BaseMediaObject;
import com.jy.sociallibrary.qq.QQShareManager;
import com.jy.sociallibrary.wb.WBShareManager;
import com.jy.sociallibrary.wx.WXListener;
import com.jy.sociallibrary.wx.WXShareManager;

/**
 * Administrator
 * created at 2016/11/9 17:15
 * TODO:第三方分享
 */
public class ShareHelper {

    public Context mContext;
    //QQ分享
    private QQShareManager qqShareManager;
    //微信分享
    private WXShareManager wxShareManager;
    //微博分享
    private WBShareManager wbShareManager;

    //回调接口
    private OnSocialSdkShareListener listener;


    public ShareHelper(Context context, OnSocialSdkShareListener listener) {
        this.mContext = context;
        this.listener = listener;
        initialize();
    }

    private void initialize() {
        qqShareManager = new QQShareManager(mContext);
        qqShareManager.setQqShareListener(listener);
        wxShareManager = new WXShareManager(mContext);
        wbShareManager = new WBShareManager(mContext, listener);
    }

    /**
     * 检查微信是否安装回调
     *
     * @param listener
     */
    public void setWXListener(WXListener listener) {
        wxShareManager.setListener(listener);
    }

    /**
     * onActivityResults处调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void result2Activity(int requestCode, int resultCode, Intent data) {
        try {
            if (qqShareManager != null) {
                qqShareManager.result2Activity(requestCode, resultCode, data);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        doResultIntent(data);
    }

    /**
     * Activity
     * onCreate处调用
     * onNewIntent处调用
     *
     * @param data
     */
    public void doResultIntent(Intent data) {
        try {
            if (wbShareManager != null) {
                wbShareManager.doResultIntent(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void share(int sharePlatform, BaseMediaObject media) {
        switch (sharePlatform) {
            case SDKSharePlatform.QQ_QZONE:
                qqShareManager.doShareAll(media, true);
                break;
            case SDKSharePlatform.QQ_FRIENDS:
                qqShareManager.doShareAll(media, false);
                break;
            case SDKSharePlatform.WX_CB:
                wxShareManager.doShareAll(media, true);
                break;
            case SDKSharePlatform.WX_FRIENDS:
                wxShareManager.doShareAll(media, false);
                break;
            case SDKSharePlatform.WB:
                wbShareManager.doShareAll(media);
                break;
        }
    }
}
