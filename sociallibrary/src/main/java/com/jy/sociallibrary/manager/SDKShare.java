package com.jy.sociallibrary.manager;

import android.content.Context;
import android.content.Intent;

import com.jy.sociallibrary.bean.ShareInfo;
import com.jy.sociallibrary.constant.SDKShareType;
import com.jy.sociallibrary.listener.OnSocialSdkShareListener;
import com.jy.sociallibrary.qq.QQShareManager;
import com.jy.sociallibrary.wb.WBShareManager;
import com.jy.sociallibrary.wx.WXListener;
import com.jy.sociallibrary.wx.WXShareManager;

/**
 * Administrator
 * created at 2016/11/9 17:15
 * TODO:第三方分享
 */
public class SDKShare {

    public Context mContext;
    //QQ分享
    private QQShareManager qqShareManager;
    //微信分享
    private WXShareManager wxShareManager;
    //微博分享
    private WBShareManager wbShareManager;

    //回调接口
    private OnSocialSdkShareListener listener;


    public SDKShare(Context context, OnSocialSdkShareListener listener) {
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


    public void share(int shareType, ShareInfo shareInfo) {
        switch (shareType) {
            case SDKShareType.TYPE_QQ_QZONE:
                qqShareManager.doShareAll(shareInfo.title, shareInfo.summary, shareInfo.targetUrl, shareInfo.imageUrl, shareInfo.appName, true);
                break;
            case SDKShareType.TYPE_QQ_FRIENDS:
                qqShareManager.doShareAll(shareInfo.title, shareInfo.summary, shareInfo.targetUrl, shareInfo.imageUrl, shareInfo.appName, false);
                break;
            case SDKShareType.TYPE_WX_CB:
                wxShareManager.shareWeb(shareInfo.targetUrl, shareInfo.title, shareInfo.summary, shareInfo.bitmap, true);
                break;
            case SDKShareType.TYPE_WX_FRIENDS:
                wxShareManager.shareWeb(shareInfo.targetUrl, shareInfo.title, shareInfo.summary, shareInfo.bitmap, false);
                break;
            case SDKShareType.TYPE_WB:
                wbShareManager.shareMultiMsg(shareInfo.title, shareInfo.bitmap, shareInfo.title, shareInfo.summary, shareInfo.summary, shareInfo.targetUrl, shareInfo.bitmap);
                break;
        }
    }
}
