package com.jy.sociallibrary.media;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.utils.SDKLogUtils;

import java.io.File;

/**
 * @description 分享图片媒体、缩略图媒体 ，注意：分享大图建议使用byte数组，因为intent传输有限制，导致bitmap无法获取
 * @date: 2021/3/30 15:36
 * @author: jy
 */
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

    public JYImage(byte[] bytes) {
        this.mObject = bytes;
        init(bytes);
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
        if (mObject instanceof File) {
            imageType = SDKImageType.URL_IMAGE;
        } else if (mObject instanceof String) {
            imageType = SDKImageType.URL_IMAGE;
        } else if (mObject instanceof Integer) {
            imageType = SDKImageType.RES_IMAGE;
        } else if (mObject instanceof Bitmap) {
            imageType = SDKImageType.BITMAP_IMAGE;
        } else if (mObject instanceof byte[]) {
            imageType = SDKImageType.BYTE_ARRAY;
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
            case SDKImageType.BYTE_ARRAY:
                mObject = in.createByteArray();
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
            case SDKImageType.BYTE_ARRAY:
                dest.writeByteArray((byte[]) mObject);
                break;
        }
    }

}
