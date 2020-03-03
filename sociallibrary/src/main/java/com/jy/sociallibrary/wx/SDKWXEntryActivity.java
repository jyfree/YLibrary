package com.jy.sociallibrary.wx;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jy.sociallibrary.SDKConfig;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Administrator
 * created at 2019/9/29 13:59
 * TODO:微信分析或登录回调activity
 */
public abstract class SDKWXEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SDKConfig.getWx_appID(), false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        SDKLogUtils.i("微信登录|分享授权--onReq--openId", baseReq.openId);
    }

    @Override
    public void onResp(BaseResp resp) {

        if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {//分享
            handleRespShare(resp);
        } else if (ConstantsAPI.COMMAND_SENDAUTH == resp.getType()) {//登录
            handleRespLogin((SendAuth.Resp) resp);
        }

    }


    private void handleRespLogin(SendAuth.Resp resp) {
        SDKLogUtils.i("微信登录授权--onResp", "errCode", resp.errCode, "errStr", resp.errStr, "state", resp.state);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                authSucceed(resp.code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                authCancel();
                break;
            default:
                authFail(resp.errCode);
                break;
        }
        finish();

    }

    private void handleRespShare(BaseResp resp) {
        SDKLogUtils.i("微信分享授权--onResp", "errCode", resp.errCode, "errStr", resp.errStr, "transaction", resp.transaction);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                shareSucceed();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                shareCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            default:
                shareFail(resp.errCode);
                break;
        }
        finish();
    }

    //登录授权成功
    public abstract void authSucceed(String code);

    //取消登录授权
    public abstract void authCancel();

    //登录授权失败
    public abstract void authFail(int errCode);

    //分享成功
    public abstract void shareSucceed();

    //取消分享
    public abstract void shareCancel();

    //分享失败
    public abstract void shareFail(int errCode);
}