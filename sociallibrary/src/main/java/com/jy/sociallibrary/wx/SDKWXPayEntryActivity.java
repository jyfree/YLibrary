package com.jy.sociallibrary.wx;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jy.sociallibrary.SDKConfig;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public abstract class SDKWXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SDKConfig.getWx_appID());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        SDKLogUtils.i("微信支付授权--onReq--openId", req.openId);
        finish();
    }


    @Override
    public void onResp(BaseResp resp) {
        SDKLogUtils.i("微信支付授权--onResp，errCode:", resp.errCode, "openid：", resp.openId, "type:", resp.getType());

        //支付成功
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            switch (resp.errCode) {
                case 0:
                    paySucceed();
                    break;
                case -2:
                    payCancel();
                    break;
                default:
                    payFail(resp.errCode);
                    break;
            }
        }
        finish();
    }

    public abstract void paySucceed();

    public abstract void payCancel();

    public abstract void payFail(int errCode);

}