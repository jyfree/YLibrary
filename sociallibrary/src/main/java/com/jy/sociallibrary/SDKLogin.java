package com.jy.sociallibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.jy.sociallibrary.listener.OnSocialSdkLoginListener;
import com.jy.sociallibrary.qq.QQChannelManager;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.jy.sociallibrary.wb.WBChannelManager;
import com.jy.sociallibrary.wx.WXChannelManager;
import com.jy.sociallibrary.wx.WXListener;

/**
 * Administrator
 * created at 2016/2/24 9:47
 * TODO:第三方登录sdk
 */
public class SDKLogin {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private OnSocialSdkLoginListener loginListener;

    public SDKLogin(Context context, OnSocialSdkLoginListener listener) {

        this.mContext = context;
        this.loginListener = listener;
        initProgressDialog();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(mContext.getString(R.string.social_sdk_extLogin_channel_loging));
        mProgressDialog.setCancelable(false);
    }

    public void showProgressDialog(boolean isShow) {
        try {
            if (mProgressDialog == null || mContext == null)
                return;
            if (isShow) {
                mProgressDialog.show();
            } else {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            SDKLogUtils.e(e.getMessage());
        }
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
            if (qqChannelManager != null) {
                qqChannelManager.result2Activity(requestCode, resultCode, data);
            }
            if (wbChannelManager != null) {
                wbChannelManager.result2Activity(requestCode, resultCode, data);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //***************************qq登录***************************88
    private QQChannelManager qqChannelManager;

    /**
     * qq登录
     */
    public void qqLogin() {
        if (qqChannelManager == null) {
            qqChannelManager = new QQChannelManager(mContext);
            qqChannelManager.setLoginListener(loginListener);
        }
        qqChannelManager.loginQQ();
    }

    //*********************微博登录*******************
    private WBChannelManager wbChannelManager;

    /**
     * 微博登录
     */
    public void wbLogin() {
        if (wbChannelManager == null) {
            wbChannelManager = new WBChannelManager(mContext, loginListener);
        }
        wbChannelManager.wbLogin();
    }


    //****************微信登录**********************
    private WXChannelManager wxChannelManager;

    private void initWXChannelManager() {
        if (wxChannelManager == null) {
            wxChannelManager = new WXChannelManager(mContext);
        }
    }

    public void wxLogin() {
        initWXChannelManager();
        wxChannelManager.wxLogin();
    }

    /**
     * 检查微信是否安装回调
     *
     * @param listener
     */
    public void setWXListener(WXListener listener) {
        initWXChannelManager();
        wxChannelManager.setListener(listener);
    }

}
