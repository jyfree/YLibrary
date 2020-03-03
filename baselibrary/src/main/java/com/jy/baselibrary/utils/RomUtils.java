package com.jy.baselibrary.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jy.baselibrary.sp.SharedPreferencesConfigUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Administrator
 * created at 2018/3/17 10:44
 * TODO:判断手机ROM 手机os
 */
public class RomUtils {

    private static final String TAG = "RomUtil";

    public static final String ROM_MIUI = "MIUI";//小米
    public static final String ROM_EMUI = "EMUI";//华为
    public static final String ROM_FLYME = "FLYME";//魅族
    public static final String ROM_OPPO = "OPPO";//OPPO
    public static final String ROM_SMARTISAN = "SMARTISAN";//锤子
    public static final String ROM_VIVO = "VIVO";//VIVO
    public static final String ROM_QIKU = "QIKU";//360

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";//小米
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";//华为
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";//OPPO
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";//锤子
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";//VIVO

    private static String sName;
    private static String sVersion;

    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check("360");
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    private static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }
        String tmpName = SharedPreferencesConfigUtils.getInstance(BaseUtils.getApp()).getString(SharedPreferencesConfigUtils.ROM);
        String tmpVersion = SharedPreferencesConfigUtils.getInstance(BaseUtils.getApp()).getString(SharedPreferencesConfigUtils.ROM_VERSION);
        if (null != tmpName && !tmpName.isEmpty()) {
            sName = tmpName;
            sVersion = tmpVersion;
            YLogUtils.INSTANCE.iFormatTag(TAG, "sp--name：%s--version：%s---rom：%s", sName, sVersion, rom);
            return sName.equals(rom);
        }
        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        SharedPreferencesConfigUtils.getInstance(BaseUtils.getApp()).setString(SharedPreferencesConfigUtils.ROM, sName);
        SharedPreferencesConfigUtils.getInstance(BaseUtils.getApp()).setString(SharedPreferencesConfigUtils.ROM_VERSION, sVersion);

        YLogUtils.INSTANCE.iFormatTag(TAG, "getProp--name：%s--version：%s---rom：%s", sName, sVersion, rom);
        return sName.equals(rom);
    }

    private static String getProp(String name) {
        String line;
        BufferedReader input = null;
        Process mProcess = null;
        try {
            mProcess = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(mProcess.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read prop " + name, ex);
            return null;
        } finally {
            if (mProcess != null) {
                mProcess.destroy();
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }
}
