package com.jy.commonlibrary.aspect;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.jy.baselibrary.utils.BaseUtils;


/**
 * @Author Administrator
 * @Date 2019/11/8-15:47
 * @TODO 使用aspect切面，需要注意：若需要获取注解信息，则定义为RUNTIME注解，否则定义为CLASS注解
 */
public class AspectUtils {

    public static final String TAG = "Aspect";

    /**
     * 通过对象获取上下文
     */
    public static Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return BaseUtils.getApp();
    }
}
