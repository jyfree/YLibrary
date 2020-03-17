package com.jy.sociallibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jy.sociallibrary.constant.SDKImageType;
import com.jy.sociallibrary.media.JYImage;

public class JYImageUtils {

    public static Bitmap getImageUrl(Context context, JYImage jyImage) {
        Bitmap bitmap = null;
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
        return bitmap;
    }
}
