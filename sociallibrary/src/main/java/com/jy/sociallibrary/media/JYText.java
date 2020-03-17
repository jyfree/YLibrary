package com.jy.sociallibrary.media;

import android.os.Parcel;

public class JYText extends BaseMediaObject {
    public String content;//内容

    public JYText(String content) {
        this.content = content;
    }

    protected JYText(Parcel in) {
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JYText> CREATOR = new Creator<JYText>() {
        @Override
        public JYText createFromParcel(Parcel in) {
            return new JYText(in);
        }

        @Override
        public JYText[] newArray(int size) {
            return new JYText[size];
        }
    };
}
