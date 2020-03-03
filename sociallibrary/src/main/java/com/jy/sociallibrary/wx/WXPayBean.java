package com.jy.sociallibrary.wx;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Administrator
 * created at 2016/2/25 16:11
 * TODO:微信支付实体类
 */
public class WXPayBean implements Parcelable {
    private String partnerId;//商户号
    private String prepayId;//预支付交易会话标识
    private String packageValue;//固定为“Sign=wxpay”
    private String nonceStr;//随机字符串
    private String timeStamp;//时间戳
    private String sign;//签名
    private String orderId;//服务器订单号

    public WXPayBean() {

    }

    protected WXPayBean(Parcel in) {
        partnerId = in.readString();
        prepayId = in.readString();
        packageValue = in.readString();
        nonceStr = in.readString();
        timeStamp = in.readString();
        sign = in.readString();
        orderId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partnerId);
        dest.writeString(prepayId);
        dest.writeString(packageValue);
        dest.writeString(nonceStr);
        dest.writeString(timeStamp);
        dest.writeString(sign);
        dest.writeString(orderId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WXPayBean> CREATOR = new Creator<WXPayBean>() {
        @Override
        public WXPayBean createFromParcel(Parcel in) {
            return new WXPayBean(in);
        }

        @Override
        public WXPayBean[] newArray(int size) {
            return new WXPayBean[size];
        }
    };

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }


    public String getOrderId() {
        return orderId;
    }


    public JSONObject buildJson() {
        JSONObject json = null;
        try {
            json = new JSONObject();

            json.put("partnerId", partnerId);
            json.put("prepayId", prepayId);
            json.put("packageValue", packageValue);
            json.put("nonceStr", nonceStr);
            json.put("timeStamp", timeStamp);
            json.put("sign", sign);
            json.put("orderId", orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static WXPayBean parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return WXPayBean.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static WXPayBean parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        WXPayBean wxPayBean = new WXPayBean();

        wxPayBean.partnerId = jsonObject.optString("partnerId");
        wxPayBean.prepayId = jsonObject.optString("prepayId");
        wxPayBean.packageValue = jsonObject.optString("packageValue");
        wxPayBean.nonceStr = jsonObject.optString("nonceStr");
        wxPayBean.timeStamp = jsonObject.optString("timeStamp");
        wxPayBean.sign = jsonObject.optString("sign");
        wxPayBean.orderId = jsonObject.optString("orderId");

        return wxPayBean;
    }


    public static String getShortName() {
        return WXPayBean.class.getSimpleName().toLowerCase();
    }


}
