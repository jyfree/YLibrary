package com.jy.sociallibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.media.JYImage;

public class JYImageUtils {

    public static Bitmap getImageBitmap(Context context, JYImage jyImage) {
        Bitmap bitmap = null;
        if (jyImage == null) {
            SDKLogUtils.e("getImageBitmap--JYImage is null");
            return null;
        }
        if (jyImage.mObject == null) {
            SDKLogUtils.e("getImageBitmap--JYImage mObject is null");
            return null;
        }
        switch (jyImage.imageType) {
            case SDKImageType.URL_IMAGE:
                bitmap = SDKBitmapUtils.getBitmap(jyImage.mObject.toString());
                break;
            case SDKImageType.BITMAP_IMAGE:
                bitmap = (Bitmap) jyImage.mObject;
                break;
            case SDKImageType.RES_IMAGE:
                bitmap = SDKBitmapUtils.getBitmap(context, (Integer) jyImage.mObject);
                break;
        }
        SDKLogUtils.i("getImageBitmap--bitmap:", bitmap);
        return bitmap;
    }

    public static String getImagePath(JYImage jyImage) {
        if (jyImage == null) {
            SDKLogUtils.e("getImagePath--JYImage is null");
            return "";
        }
        if (jyImage.mObject == null) {
            SDKLogUtils.e("getImagePath--JYImage mObject is null");
            return "";
        }
        return jyImage.mObject.toString();
    }
}
