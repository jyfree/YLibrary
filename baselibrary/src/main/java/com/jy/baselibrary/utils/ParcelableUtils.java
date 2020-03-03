package com.jy.baselibrary.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUtils {
    public static byte[] marShall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static <T> T unMarShall(String str, Parcelable.Creator<T> creator) {
        // 1.解码
        byte[] bytes = EncodeUtils.base64Decode(str);
        // 2.反序列化
        Parcel parcel = unMarShall(bytes);
        return creator.createFromParcel(parcel);
    }

    public static Parcel unMarShall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }
}
