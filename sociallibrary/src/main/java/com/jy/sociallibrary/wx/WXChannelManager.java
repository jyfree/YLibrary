package com.jy.sociallibrary.wx;

import android.content.Context;

import com.jy.sociallibrary.SDKConfig;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Administrator
 * created at 2016/2/24 17:49
 * TODO:微信登录
 */
public class WXChannelManager {

    public IWXAPI mWxAPI;

    private Context mContext;

    private WXListener listener;

    public WXChannelManager(Context context) {
        this.mContext = context;
        init();
    }

    public void setListener(WXListener listener) {
        this.listener = listener;
    }

    private void init() {
        if (mWxAPI == null) {
            mWxAPI = WXAPIFactory.createWXAPI(mContext, SDKConfig.getWx_appID(), false);
        }
        mWxAPI.registerApp(SDKConfig.getWx_appID());
    }

    /**
     * 微信登录
     */
    public void wxLogin() {
        if (!checkInstallWX()) {
            return;
        }
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        mWxAPI.sendReq(req);
    }


    /**
     * 检测微信
     */
    public boolean checkInstallWX() {
        if (!mWxAPI.isWXAppInstalled()) {
            //提醒用户没有安装微信微信
            if (listener != null) {
                listener.installWXAPP();
            }
            return false;
        }
        return true;
    }

}
