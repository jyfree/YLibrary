package com.jy.sociallibrary.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Administrator
 * created at 2016/11/10 16:16
 * TODO:分享实体
 */
public class ShareInfo implements Parcelable {

    public String title;//标题
    public String targetUrl;//目标地址
    public String summary;//内容
    public String imageUrl;//图片URL
    public String appName;//app名称
    public Bitmap bitmap;//图片对象

    public ShareInfo() {

    }

    public ShareInfo(Parcel in) {
        title = in.readString();
        targetUrl = in.readString();
        summary = in.readString();
        imageUrl = in.readString();
        appName = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(targetUrl);
        dest.writeString(summary);
        dest.writeString(imageUrl);
        dest.writeString(appName);
        dest.writeParcelable(bitmap, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareInfo> CREATOR = new Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel in) {
            return new ShareInfo(in);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
