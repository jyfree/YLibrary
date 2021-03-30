package com.jy.sociallibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Closeable;
import java.io.InputStream;

public class SDKBitmapUtils {

    private static int MAX_WIDTH = 768;
    private static int MAX_HEIGHT = 1024;

    public static Bitmap getBitmap(Context context, int resId) {
        if (resId != 0 && context != null) {
            InputStream inputStream = null;

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                inputStream = context.getResources().openRawResource(resId);
                BitmapFactory.decodeStream(inputStream, null, options);
                close(inputStream);
                int sampleSize = (int) sampleSize((float) options.outWidth, (float) options.outHeight, (float) MAX_WIDTH, (float) MAX_HEIGHT);
                if (sampleSize > 0) {
                    options.inSampleSize = sampleSize;
                }

                options.inJustDecodeBounds = false;
                inputStream = context.getResources().openRawResource(resId);
                return BitmapFactory.decodeStream(inputStream, null, options);
            } catch (OutOfMemoryError e) {
                SDKLogUtils.e(e.getMessage());
            } catch (Exception e) {
                SDKLogUtils.e(e.getMessage());
            } finally {
                close(inputStream);
            }

            return null;
        } else {
            return null;
        }
    }


    public static Bitmap getBitmap(String path) {
        try {
            return BitmapFactory.decodeFile(path);
        } catch (OutOfMemoryError e) {
            SDKLogUtils.e(e.getMessage());
            return null;
        }
    }

    public static Bitmap getBitmap(byte[] bytes) {
        try {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (OutOfMemoryError e) {
            SDKLogUtils.e(e.getMessage());
            return null;
        }
    }

    private static float sampleSize(float width, float height, float maxWidth, float maxHeight) {
        if (width <= maxHeight && height <= maxHeight) {
            return -1.0F;
        } else {
            float wRatio = width / maxWidth;
            float hRatio = height / maxHeight;
            return Math.max(wRatio, hRatio);
        }
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            SDKLogUtils.e(e.getMessage());
        }

    }
}
