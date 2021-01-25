package com.jy.sociallibrary.wb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.jy.sociallibrary.SDKConfig;
import com.jy.sociallibrary.constant.SDKLoginType;
import com.jy.sociallibrary.listener.OnSocialSdkLoginListener;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;


/**
 * Administrator
 * created at 2016/2/17 17:11
 * TODO:用于微博登录|分享等
 */
public class WBChannelManager {

    private Context mContext;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用户信息接口
     */
    private UsersAPI mUsersAPI;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    private OnSocialSdkLoginListener listener;

    public WBChannelManager(Context context, OnSocialSdkLoginListener listener) {
        this.mContext = context;
        this.listener = listener;
        SDKConfig.initWB();
        init();

    }

    private void init() {
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mSsoHandler = new SsoHandler((Activity) mContext);
    }


    /**
     * SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
     */
    public void wbLogin() {
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 退出登录
     */
    public void wbLogout() {
        if (mContext == null) return;
        AccessTokenKeeper.clear(mContext.getApplicationContext());
    }

    /**
     * SSO 授权回调
     * 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
     * <p/>
     * onActivityResults处调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void result2Activity(int requestCode, int resultCode, Intent data) {

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #result2Activity} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {

            SDKLogUtils.i("微博登录授权--成功", oauth2AccessToken);
            //从这里获取用户输入的 电话号码信息
//            String phoneNum = oauth2AccessToken.getPhoneNum();
            if (oauth2AccessToken.isSessionValid()) {

                SDKLogUtils.i("微博登录授权--成功--session有效", oauth2AccessToken.getToken());

                listener.loginAuthSuccess(SDKLoginType.TYPE_WB, oauth2AccessToken.getToken(), oauth2AccessToken.getUid());
                // 保存 Token 到 SharedPreferences
                if (mContext == null) return;
                AccessTokenKeeper.writeAccessToken(mContext, oauth2AccessToken);
            } else {
                SDKLogUtils.e("微博登录授权--成功--session失效", oauth2AccessToken.getToken());
                listener.loginFail(SDKLoginType.TYPE_WB, "session失效");
            }
        }

        @Override
        public void cancel() {
            SDKLogUtils.i("微博登录授权--取消");
            listener.loginCancel(SDKLoginType.TYPE_WB);
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            SDKLogUtils.e("微博登录授权--失败--errorCode", wbConnectErrorMessage.getErrorCode(), "errorMessage", wbConnectErrorMessage.getErrorMessage());
            listener.loginFail(SDKLoginType.TYPE_WB, wbConnectErrorMessage.getErrorMessage());
        }

    }


    public void getUserInfo() {
        if (mContext == null) return;
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        // 获取用户信息接口
        mUsersAPI = new UsersAPI(mContext, SDKConfig.getWb_appID(), mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, userListener);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener userListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                SDKLogUtils.i("wb--RequestListener", response);
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    //TODO 获取用户信息成功
                } else {
                    if (null == mContext) return;
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            SDKLogUtils.e("wb--RequestListener", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            if (info == null || mContext == null) return;
            Toast.makeText(mContext, info.toString(), Toast.LENGTH_LONG).show();
        }
    };


}
