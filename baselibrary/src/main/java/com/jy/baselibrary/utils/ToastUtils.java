package com.jy.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * @description toast工具类
 * @date: 2021/4/7 14:22
 * @author: jy
 */
public class ToastUtils {
    private final static String TAG = ToastUtils.class.getSimpleName();

    private ToastUtils() {
    }

    /**
     * 显示时间短的Toast
     *
     * @param msg 显示的内容
     */
    public static Toast showToast(CharSequence msg) {
        return showToast(msg, Toast.LENGTH_SHORT);
    }


    /**
     * 显示时间短的Toast
     *
     * @param resId 显示的资源ID
     */
    public static Toast showToast(@StringRes int resId) {
        return showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间短的Toast
     *
     * @param msg 显示的内容
     */
    public static Toast showToastShortWithGravity(CharSequence msg, int gravity) {
        return showToast(msg, Toast.LENGTH_SHORT, gravity, 0, 0);
    }

    /**
     * 显示时间短的Toast
     *
     * @param resId 显示的资源ID
     */
    public static Toast showToastShortWithGravity(@StringRes int resId, int gravity) {
        return showToast(resId, Toast.LENGTH_SHORT, gravity, 0, 0);
    }

    /**
     * 显示时间长的Toast
     *
     * @param msg 显示的内容
     */
    public static Toast showToastLong(CharSequence msg) {
        return showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示时间长的Toast
     *
     * @param resId 显示的资源ID
     */
    public static Toast showToastLong(@StringRes int resId) {
        return showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示时间长的Toast
     *
     * @param msg     显示的内容
     * @param gravity 要显示的文本重心
     */
    public static Toast showToastLongWithGravity(CharSequence msg, int gravity) {
        return showToast(msg, Toast.LENGTH_LONG, gravity, 0, 0);
    }

    /**
     * 显示时间长的Toast
     *
     * @param resId   显示的资源ID
     * @param gravity 要显示的文本重心
     */
    public static Toast showToastLongWithGravity(@StringRes int resId, int gravity) {
        return showToast(resId, Toast.LENGTH_LONG, gravity, 0, 0);
    }

    /**
     * 显示时间短的Toast
     *
     * @param resId   显示的资源ID
     * @param gravity 要显示的文本重心
     * @param bgResId 背景颜色
     */
    public static Toast showToastShortWithGravity(@StringRes int resId, int gravity, @DrawableRes int bgResId) {
        return showToast(resId, Toast.LENGTH_SHORT, gravity, 0, 0, bgResId);
    }

    /**
     * 显示时间短的Toast
     *
     * @param text    显示的文字
     * @param gravity 要显示的文本重心
     * @param bgResId 背景颜色
     */
    public static Toast showToastShortWithGravity(final CharSequence text, int gravity, @DrawableRes int bgResId) {
        return showToast(text, Toast.LENGTH_SHORT, gravity, 0, 0, bgResId);
    }


    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param resId    显示的资源ID
     * @param duration 时长
     */
    public static Toast showToast(@StringRes int resId, int duration) {
        String text = getString(resId);
        return showToast(text, duration, Gravity.NO_GRAVITY, 0, 0, 0);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param text     要显示的toast内容
     * @param duration 时长
     */
    public static Toast showToast(final CharSequence text, final int duration) {
        return showToast(text, duration, Gravity.NO_GRAVITY, 0, 0, 0);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param resId    显示的资源ID
     * @param duration 时长
     * @param gravity  要显示的文本重心
     * @param offsetX
     * @param offsetY
     */
    private static Toast showToast(@StringRes final int resId,
                                   final int duration, final int gravity, final int offsetX, final int offsetY) {
        String text = getString(resId);
        return showToast(text, duration, gravity, offsetX, offsetY, 0);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param text     要显示的toast内容
     * @param duration 时长
     * @param gravity  要显示的文本重心
     * @param offsetX
     * @param offsetY
     */
    private static Toast showToast(final CharSequence text, final int duration,
                                   final int gravity, final int offsetX, final int offsetY) {
        return showToast(text, duration, gravity, offsetX, offsetY, 0);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param resId    显示的资源ID
     * @param duration 时长
     * @param gravity  要显示的文本重心
     * @param offsetX
     * @param offsetY
     * @param bgResId  背景颜色
     */
    private static Toast showToast(final int resId, final int duration, final int gravity, final int offsetX, final int offsetY, @DrawableRes final int bgResId) {
        String text = getString(resId);
        return showToast(text, duration, gravity, offsetX, offsetY, bgResId);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param text     要显示的toast内容
     * @param duration 时长
     * @param gravity  要显示的文本重心
     * @param offsetX
     * @param offsetY
     */
    private static Toast showToast(final CharSequence text, final int duration, final int gravity, final int offsetX, final int offsetY, @DrawableRes final int bgResId) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast toast = Toast.makeText(BaseUtils.getApp(), text, duration);
            if (gravity != Gravity.NO_GRAVITY) {
                toast.setGravity(gravity, offsetX, offsetY);
            }
            if (bgResId != 0) {
                toast.getView().setBackgroundResource(bgResId);
            }
            try {
                toast.show();
            } catch (Throwable throwable) {
                Log.e(TAG, "showToast()>>ToastUtils#toast问题1:" + throwable.toString());
            }
            return toast;
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(BaseUtils.getApp(), text, duration);
                    if (gravity != Gravity.NO_GRAVITY) {
                        toast.setGravity(gravity, offsetX, offsetY);
                    }
                    if (bgResId != 0) {
                        toast.getView().setBackgroundResource(bgResId);
                    }
                    try {
                        toast.show();
                    } catch (Throwable throwable) {
                        Log.e(TAG, "showToast()>>ToastUtils#toast问题2:" + throwable.toString());
                    }
                }
            });
        }
        return null;
    }


    private static Toast showToast(final View view, final int duration, final int gravity,
                                   final int offsetX, final int offsetY, @DrawableRes final int bgResId) {
        if (view == null) {
            return null;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast toast = new Toast(BaseUtils.getApp());
            toast.setDuration(duration);
            if (gravity != Gravity.NO_GRAVITY) {
                toast.setGravity(gravity, offsetX, offsetY);
            }
            if (bgResId != 0) {
                toast.getView().setBackgroundResource(bgResId);
            }
            if (view != null) {
                toast.setView(view);
            }
            try {
                toast.show();
            } catch (Throwable throwable) {
                Log.e(TAG, "showToast()>>ToastUtils#toast问题3:" + throwable.toString());
                throwable.printStackTrace();
            }
            return toast;
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = new Toast(BaseUtils.getApp());
                    toast.setDuration(duration);
                    toast.setView(view);
                    if (gravity != Gravity.NO_GRAVITY) {
                        toast.setGravity(gravity, offsetX, offsetY);
                    }
                    if (bgResId != 0) {
                        toast.getView().setBackgroundResource(bgResId);
                    }
                    try {
                        toast.show();
                    } catch (Throwable throwable) {
                        Log.e(TAG, "showToast()>>ToastUtils#toast问题4:" + throwable.toString());
                    }
                }
            });
        }
        return null;
    }

    /**
     * 获取 String 资源
     *
     * @param
     * @return
     */
    private static String getString(@StringRes int strRes) {
        String str = BaseUtils.getApp().getString(strRes);
        if (TextUtils.isEmpty(str)) {
            str = BaseUtils.getApp().getResources().getString(strRes);
        }
        return str;
    }

    /**
     * 取消toast
     */
    public static void cancelToast() {
        //方便后续更改
    }
}
