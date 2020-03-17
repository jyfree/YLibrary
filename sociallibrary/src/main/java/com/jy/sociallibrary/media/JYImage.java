package com.jy.sociallibrary.media;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.utils.SDKLogUtils;

import java.io.File;

public class JYImage extends BaseMediaObject {

    public int imageType;
    public Object mObject;
    public JYImage thumb;

    public JYImage(File file) {
        this.mObject = file.toString();
        init(file);
    }

    public JYImage(String imageUrl) {
        this.mObject = imageUrl;
        init(imageUrl);
    }

    public JYImage(Bitmap bitmap) {
        this.mObject = bitmap;
        init(bitmap);
    }

    public JYImage(int resId) {
        this.mObject = resId;
        init(resId);
    }


    protected JYImage(Parcel in) {
        imageType = in.readInt();
        thumb = in.readParcelable(JYImage.class.getClassLoader());
        mObjectParcelRead(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageType);
        dest.writeParcelable(thumb, flags);
        mObjectWriteToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JYImage> CREATOR = new Creator<JYImage>() {
        @Override
        public JYImage createFromParcel(Parcel in) {
            return new JYImage(in);
        }

        @Override
        public JYImage[] newArray(int size) {
            return new JYImage[size];
        }
    };

    private void init(Object mObject) {
        if (mObject instanceof String) {
            imageType = SDKImageType.URL_IMAGE;
        } else if (mObject instanceof Integer) {
            imageType = SDKImageType.RES_IMAGE;
        } else if (mObject instanceof Bitmap) {
            imageType = SDKImageType.BITMAP_IMAGE;
        } else {
            SDKLogUtils.e("不支持的JYImage构建类型，您传入的类型为:", mObject.getClass().getSimpleName());
        }
    }

    private void mObjectParcelRead(Parcel in) {
        switch (imageType) {
            case SDKImageType.URL_IMAGE:
                mObject = in.readString();
                break;
            case SDKImageType.RES_IMAGE:
                mObject = in.readInt();
                break;
            case SDKImageType.BITMAP_IMAGE:
                mObject = in.readParcelable(Bitmap.class.getClassLoader());
                break;
        }
    }

    private void mObjectWriteToParcel(Parcel dest, int flags) {
        switch (imageType) {
            case SDKImageType.URL_IMAGE:
                dest.writeString(mObject.toString());
                break;
            case SDKImageType.RES_IMAGE:
                dest.writeInt((Integer) mObject);
                break;
            case SDKImageType.BITMAP_IMAGE:
                dest.writeParcelable((Bitmap) mObject, flags);
                break;
        }
    }

}
