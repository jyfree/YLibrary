package com.jy.sociallibrary.wx;

import android.content.Context;
import android.util.Xml;

import com.jy.sociallibrary.SDKConfig;
import com.jy.sociallibrary.SDKThreadManager;
import com.jy.sociallibrary.utils.SDKLogUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class WXPayManager {

    private PayReq req;
    private IWXAPI msgApi;
    private Map<String, String> resultUnifiedOrder;
    private Context context;
    private WXListener listener;

    public WXPayManager(Context context) {
        this.context = context;
        initPay();
    }

    public void setListener(WXListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化支付
     */
    private void initPay() {
        msgApi = WXAPIFactory.createWXAPI(context, null);
        req = new PayReq();
        msgApi.registerApp(SDKConfig.getWx_appID());

    }


    /**
     * APP支付生成预支付订单
     */
    public void localPayTask() {

        SDKThreadManager.executeOnNetWorkThread(new Runnable() {
            @Override
            public void run() {
                String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
                String entity = genProductArgs();
                byte[] buf = HttpUtils.httpPost(url, entity);

                SDKLogUtils.i("wxPay--localPayTask", buf);

                if (buf == null) return;
                final String content = new String(buf);

                SDKLogUtils.i("wxPay--localPayTask", "entity", entity, "content", content);

                SDKThreadManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        resultUnifiedOrder = decodeXml(content);
                        SDKLogUtils.d("wxPay--localPayTask", "resultUnifiedOrder", resultUnifiedOrder);
                        prePay();
                        pay();
                    }
                });
            }
        });

    }


    /**
     * 生成APP微信支付参数
     */
    public void prePay() {
        genPayReq(null);
    }

    /**
     * 调起微信支付
     */
    public void pay() {
        sendPayReq();
    }


    /**
     * 预支付订单生成签名
     */
    private String genPackageSign(List<WXBasicNameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(SDKConfig.getWx_ApiKey());

        String packageSign = MD5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        SDKLogUtils.i("wxPay--genPackageSign", packageSign);

        return packageSign;
    }


    /**
     * 支付订单生成签名（最终签名）
     *
     * @param params
     * @return
     */
    private String genAppSign(List<WXBasicNameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(SDKConfig.getWx_ApiKey());

        String appSign = MD5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        SDKLogUtils.i("wxPay--genAppSign", appSign, params.size());

        return appSign;
    }

    /**
     * 生成XML格式的支付参数
     *
     * @param params
     * @return
     */
    private String toXml(List<WXBasicNameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        SDKLogUtils.i("wxPay--toXml", sb.toString());
        return sb.toString();
    }

    /**
     * 解析预支付订单返回的参数
     *
     * @param content
     * @return
     */
    private Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (!"xml".equals(nodeName)) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            SDKLogUtils.e("wxPay--decodeXml", e.toString());
        }
        return null;

    }


    /**
     * 生成随机字符串
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成订单时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     * 生成唯一订单号
     *
     * @return
     */
    private String genOutTradNo() {
        Random random = new Random();
        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    /**
     * 生成预支付订单参数
     *
     * @return
     */
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<WXBasicNameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new WXBasicNameValuePair("appid", SDKConfig.getWx_appID()));
            packageParams.add(new WXBasicNameValuePair("body", "金币充值"));
            packageParams.add(new WXBasicNameValuePair("mch_id", SDKConfig.getWx_MchID()));
            packageParams.add(new WXBasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new WXBasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new WXBasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new WXBasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new WXBasicNameValuePair("total_fee", "1"));
            packageParams.add(new WXBasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new WXBasicNameValuePair("sign", sign));


            String xmlString = toXml(packageParams);

            //改变拼接之后xml字符串格式
            return new String(xmlString.getBytes(), "ISO8859-1");

        } catch (Exception e) {
            SDKLogUtils.e("wxPay--genProductArgs", "genProductArgs fail, ex = ", e.getMessage());
            return null;
        }


    }

    /**
     * 生成调起微信支付所需要的支付参数
     *
     * @param wxPayBean
     */
    public void genPayReq(WXPayBean wxPayBean) {

        if (wxPayBean != null) {
            req.appId = SDKConfig.getWx_appID();
            req.partnerId = wxPayBean.getPartnerId();
            req.prepayId = wxPayBean.getPrepayId();
            req.packageValue = wxPayBean.getPackageValue();
            req.nonceStr = wxPayBean.getNonceStr();
            req.timeStamp = wxPayBean.getTimeStamp();


        } else {
            req.appId = SDKConfig.getWx_appID();
            req.partnerId = SDKConfig.getWx_MchID();
            if (resultUnifiedOrder != null) {
                req.prepayId = resultUnifiedOrder.get("prepay_id");
            }
            req.packageValue = "Sign=WXPay";
            req.nonceStr = genNonceStr();
            req.timeStamp = String.valueOf(genTimeStamp());


        }
        List<WXBasicNameValuePair> signParams = new LinkedList<>();
        signParams.add(new WXBasicNameValuePair("appid", req.appId));
        signParams.add(new WXBasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new WXBasicNameValuePair("package", req.packageValue));
        signParams.add(new WXBasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new WXBasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new WXBasicNameValuePair("timestamp", req.timeStamp));

        if (wxPayBean != null) {
            req.sign = wxPayBean.getSign();
        } else {
            //二次签名
            req.sign = genAppSign(signParams);
        }

    }

    /**
     * 调起微信支付
     */
    private void sendPayReq() {
        if (!checkInstallWX()) {
            return;
        }
        msgApi.registerApp(SDKConfig.getWx_appID());
        msgApi.sendReq(req);
    }


    /**
     * 检测微信
     */
    private boolean checkInstallWX() {
        if (!msgApi.isWXAppInstalled()) {
            //提醒用户没有安装微信微信
            if (listener != null) {
                listener.installWXAPP();
            }
            return false;
        }
        return true;
    }
}

