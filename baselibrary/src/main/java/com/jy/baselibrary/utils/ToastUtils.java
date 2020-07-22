package com.jy.baselibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * @Author Administrator
 * @Date 2019/5/30-17:35
 * @TODO toast工具类
 * 注意：避免showToast调用不当，造成Toast里的mContext内存泄漏，
 * showToast不提供Context上下文的传参，直接内部调用BaseUtils.getApp()即application
 */
public class ToastUtils {
    private static final String TAG = "ToastUtil";
    private static Toast mToast;
    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static boolean sIsHookFieldInit = false;
    private static final String FIELD_NAME_TN = "mTN";
    private static final String FIELD_NAME_HANDLER = "mHandler";


    public static void showToast(CharSequence text) {
        showToast(BaseUtils.getApp(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast(CharSequence text, int duration) {
        showToast(BaseUtils.getApp(), text, duration);
    }

    /**
     * Non-blocking showing Toast
     *
     * @param context  context，Application or Activity
     * @param text     the text show on the Toast
     * @param duration Toast.LENGTH_SHORT（default,2s） or Toast.LENGTH_LONG（3.5s）
     */
    private static void showToast(final Context context, final CharSequence text, final int duration) {
        try {
            if (context == null) return;
            ToastRunnable toastRunnable = new ToastRunnable(context, text, duration);
            if (context instanceof Activity) {
                final Activity activity = (Activity) context;
                if (!activity.isFinishing()) {
                    activity.runOnUiThread(toastRunnable);
                }
            } else {
                Handler handler = new Handler(context.getMainLooper());
                handler.post(toastRunnable);
            }
        } catch (Exception e) {
            YLogUtils.INSTANCE.eTag(TAG, e.getMessage());
        }
    }

    /**
     * cancel the toast
     */
    public static void cancelToast() {
        if (null == mToast) return;
        Looper looper = Looper.getMainLooper();
        if (looper.getThread() == Thread.currentThread()) {
            mToast.cancel();
        } else {
            new Handler(looper).post(new Runnable() {
                @Override
                public void run() {
                    mToast.cancel();
                }
            });
        }
    }

    /**
     * Hook Toast,fix the BadTokenException happened on the device 7.x while showing Toast which will cause your app to crash
     *
     * @param toast
     */
    private static void hookToast(Toast toast) {
        if (!isNeedHook()) {
            return;
        }
        try {
            if (!sIsHookFieldInit) {
                sField_TN = Toast.class.getDeclaredField(FIELD_NAME_TN);
                sField_TN.setAccessible(true);
                sField_TN_Handler = sField_TN.getType().getDeclaredField(FIELD_NAME_HANDLER);
                sField_TN_Handler.setAccessible(true);
                sIsHookFieldInit = true;
            }
            Object tn = sField_TN.get(toast);
            Handler originHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(originHandler));
        } catch (Exception e) {
            YLogUtils.INSTANCE.eTag(TAG, "Hook toast exception=", e);
        }
    }

    /**
     * Check if Toast need hook，only hook the device 7.x(api = 24/25)
     *
     * @return true for need hook to fit system bug,false for don't need hook
     */
    private static boolean isNeedHook() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.N;
    }

    private static class ToastRunnable implements Runnable {
        private Context context;
        private CharSequence text;
        private int duration;

        public ToastRunnable(Context context, CharSequence text, int duration) {
            this.context = context;
            this.text = text;
            this.duration = duration;
        }

        @SuppressLint("ShowToast")
        @Override
        public void run() {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, duration);
            } else {
                mToast.setText(text);
                mToast.setDuration(duration);
            }
            hookToast(mToast);
            mToast.show();
        }
    }

    /**
     * Safe outside Handler class which just warps the system origin handler object in the Toast.class
     */
    private static class SafelyHandlerWarpper extends Handler {
        private Handler originHandler;

        public SafelyHandlerWarpper(Handler originHandler) {
            this.originHandler = originHandler;
        }

        @Override
        public void dispatchMessage(Message msg) {
            // The outside hanlder SafelyHandlerWarpper object just catches the Exception while dispatch the message
            // if the the inside system origin hanlder object throw the BadTokenException，the outside safe SafelyHandlerWarpper object
            // just catches the exception here to avoid the app crashing
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                YLogUtils.INSTANCE.eTag(TAG, "Catch system toast exception:", e);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            //just pass the Message to the origin handler object to handle
            if (originHandler != null) {
                originHandler.handleMessage(msg);
            }
        }
    }
}
