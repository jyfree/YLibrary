package com.jy.sociallibrary.wx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jy.sociallibrary.SDKConfig;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        api.registerApp(SDKConfig.getWx_appID());
    }
}
