package com.jy.sociallibrary.bean;

/**
 * Administrator
 * created at 2016/2/26 9:44
 * TODO: 分享渠道实体类
 */
public class SDKShareChannel {

    private int platform;
    private int icon;
    private String name;

    public SDKShareChannel(int platform, int icon, String name) {
        this.platform = platform;
        this.icon = icon;
        this.name = name;
    }


    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
