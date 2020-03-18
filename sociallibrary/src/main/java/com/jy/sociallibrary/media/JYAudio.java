package com.jy.sociallibrary.media;

import android.os.Parcel;

public class JYAudio extends BaseMediaObject {
    public String webUrl;//web地址
    public String title;//标题
    public String description;//描述
    public JYImage thumb;//缩略图
    public String imageUrl;//图片路径（网络图片）
    public String audioUrl;//音频地址

    public JYAudio(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    protected JYAudio(Parcel in) {
        webUrl = in.readString();
        title = in.readString();
        description = in.readString();
        thumb = in.readParcelable(JYImage.class.getClassLoader());
        imageUrl = in.readString();
        audioUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(thumb, flags);
        dest.writeString(imageUrl);
        dest.writeString(audioUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JYAudio> CREATOR = new Creator<JYAudio>() {
        @Override
        public JYAudio createFromParcel(Parcel in) {
            return new JYAudio(in);
        }

        @Override
        public JYAudio[] newArray(int size) {
            return new JYAudio[size];
        }
    };
}
