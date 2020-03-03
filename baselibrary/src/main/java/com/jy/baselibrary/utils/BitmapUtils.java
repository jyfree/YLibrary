package com.jy.baselibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author Administrator
 * @Date 2019/10/18-14:29
 * @TODO bitmap工具
 */
public class BitmapUtils {

    /**
     * 压缩图片
     *
     * @param context   上下文
     * @param uri       图片uri
     * @param cacheFile 缓存文件
     * @param quality   压缩质量
     * @param maxSize   图片允许最大空间   单位：KB
     * @return 压缩后的bitmap缩略图
     */
    public static Bitmap imageZoom(Context context, Uri uri, File cacheFile, int quality, double maxSize) {
        Bitmap bitmapThumb = null;
        try {
            Bitmap bitMap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            //将字节换成KB
            double mid = b.length / (double) 1024;
            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
            if (mid > maxSize) {
                //获取bitmap大小 是允许最大大小的多少倍
                double i = mid / maxSize;
                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                        bitMap.getHeight() / Math.sqrt(i));
            }
            FileOutputStream out = new FileOutputStream(cacheFile);
            if (bitMap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            bitmapThumb = ThumbnailUtils.extractThumbnail(bitMap, 150, 150);
            if (!bitMap.isRecycled()) {
                bitMap.recycle();
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            YLogUtils.INSTANCE.e("压缩图片--OutOfMemoryError", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            YLogUtils.INSTANCE.e("压缩图片--Exception", e.getMessage());
        }
        return bitmapThumb;
    }

    /**
     * 压缩图片
     *
     * @param bigImage  目标bitmap
     * @param newWidth  压缩宽度
     * @param newHeight 压缩高度
     * @return
     * @throws OutOfMemoryError
     */
    public static Bitmap zoomImage(Bitmap bigImage, double newWidth, double newHeight) throws OutOfMemoryError {
        // 获取这个图片的宽和高
        float width = bigImage.getWidth();
        float height = bigImage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bigImage, 0, 0, (int) width, (int) height, matrix, true);
    }
}
