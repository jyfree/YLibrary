package com.jy.sociallibrary.media;

import android.os.Parcel;

public class JYWeb extends BaseMediaObject {

    public String webUrl;//web地址
    public String title;//标题
    public String description;//描述
    public JYImage thumb;//缩略图
    public String imageUrl;//图片路径（网络图片），qq分享需要用到，不传则缩略图为应用icon

    public JYWeb(String webUrl) {
        this.webUrl = webUrl;
    }

    protected JYWeb(Parcel in) {
        webUrl = in.readString();
        title = in.readString();
        description = in.readString();
        thumb = in.readParcelable(JYImage.class.getClassLoader());
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(thumb, flags);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JYWeb> CREATOR = new Creator<JYWeb>() {
        @Override
        public JYWeb createFromParcel(Parcel in) {
            return new JYWeb(in);
        }

        @Override
        public JYWeb[] newArray(int size) {
            return new JYWeb[size];
        }
    };
}
