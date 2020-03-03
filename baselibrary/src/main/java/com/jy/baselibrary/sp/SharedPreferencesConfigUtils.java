package com.jy.baselibrary.sp;

import android.content.Context;

/**
 * Administrator
 * created at 2018/11/20 11:35
 * TODO:存储配置信息
 * 使用前，必须在application初始化，如：
 * SharedPreferencesConfigUtils.getInstance(this).init();
 */
public class SharedPreferencesConfigUtils extends SharedPreferencesBaseUtils {

    private static SharedPreferencesConfigUtils mInstance;

    private static final String SP_NAME = "Config_File";

    public static final String SIGNATURE = "signature";//签名
    public static final String ROM = "rom";//手机ROM
    public static final String ROM_VERSION = "romVersion";//手机ROM版本

    public static SharedPreferencesConfigUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharedPreferencesConfigUtils.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferencesConfigUtils(context);
                }
            }
        }
        return mInstance;
    }

    SharedPreferencesConfigUtils(Context context) {
        super(context);
    }

    @Override
    public String getSpName() {
        return SP_NAME;
    }

    @Override
    public int getMode() {
        return Context.MODE_MULTI_PROCESS;
    }
}
