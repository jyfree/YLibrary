package com.jy.sociallibrary.bean;

/**
 * Administrator
 * created at 2016/2/26 9:44
 * TODO: 分享渠道实体类
 */
public class SDKShareChannel {

    private int id;
    private int icon;
    private String name;

    public SDKShareChannel(int id, int icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
