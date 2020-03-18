package com.jy.sociallibrary.media;

import android.os.Parcel;

public class JYVideo extends BaseMediaObject {

    public String videoUrl;//视频地址
    public String title;//标题
    public String description;//描述
    public JYImage thumb;//缩略图


    public JYVideo(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    protected JYVideo(Parcel in) {
        videoUrl = in.readString();
        title = in.readString();
        description = in.readString();
        thumb = in.readParcelable(JYImage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoUrl);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(thumb, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JYVideo> CREATOR = new Creator<JYVideo>() {
        @Override
        public JYVideo createFromParcel(Parcel in) {
            return new JYVideo(in);
        }

        @Override
        public JYVideo[] newArray(int size) {
            return new JYVideo[size];
        }
    };
}
