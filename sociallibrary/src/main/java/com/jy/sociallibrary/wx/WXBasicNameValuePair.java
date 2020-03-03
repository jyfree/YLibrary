package com.jy.sociallibrary.wx;

/**
 * Administrator
 * created at 2016/2/27 15:34
 * TODO:构造微信支付参数实体类
 */
public class WXBasicNameValuePair {

    private String name;
    private String value;

    public WXBasicNameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
