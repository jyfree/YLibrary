package com.jy.baselibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


/**
 * Administrator
 * created at 2018/11/12 15:06
 * TODO
 */
public class NetworkUtils {

    /**
     * 获取网络类型
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getNetworkTypeName() {
        String networkTypeName = "unKnow";
        try {
            ConnectivityManager mgr = (ConnectivityManager) BaseUtils.getApp().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == mgr) {
                return networkTypeName;
            }

            NetworkInfo activeNetworkInfo = mgr.getActiveNetworkInfo();

            if (activeNetworkInfo == null) {
                return networkTypeName;
            }
            if (isWifiNetWork()) {
                networkTypeName = "WIFI";
            } else {
                switch (activeNetworkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        networkTypeName = "1xRTT";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:  //电信2G
                        networkTypeName = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:  //联通或移动2G
                        networkTypeName = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        networkTypeName = "EHRPD";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0: //电信3G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //电信3G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:  //联通或移动2G
                        networkTypeName = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:  //联通3G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        networkTypeName = "HSPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP: //联通或移动2G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        networkTypeName = "HSUPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        networkTypeName = "IDEN";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:  //联通3G
                        networkTypeName = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: //4G
                        networkTypeName = "4G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            YLogUtils.INSTANCE.e(e.getMessage());
        }
        return networkTypeName;
    }

    /**
     * 判断网络是2g/3g,或者是wifi环境
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifiNetWork() {
        try {

            ConnectivityManager connectMgr = (ConnectivityManager) BaseUtils.getApp().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == connectMgr) {
                return false;
            }
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            YLogUtils.INSTANCE.e(e.getMessage());
            return false;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) BaseUtils.getApp().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception e) {
            YLogUtils.INSTANCE.e(e.getMessage());
            return false;
        }
    }
}
