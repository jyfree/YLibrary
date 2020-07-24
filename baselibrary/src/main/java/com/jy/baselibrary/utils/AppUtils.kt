package com.jy.baselibrary.utils

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Process
import com.jy.baselibrary.sp.SharedPreferencesConfigUtils
import java.io.File


object AppUtils {
    /**
     * 获取当前进程名
     */
    fun getCurProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return null
    }

    /**
     * 根据进程id，获取进程名
     */
    fun getCurProcessName(context: Context, pid: Int): String? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return null
    }

    /**
     * 安装App（支持7.0）
     * 8.0 需要manifest注册权限：android.permission.REQUEST_INSTALL_PACKAGES
     * @param file      文件
     * @param authority 7.0及以上安装需要传入清单文件中的`<provider>`的authorities属性
     * <br></br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     */
    fun installApp(file: File, authority: String) {
        if (!FileUtils.isFileExists(file)) return
        BaseUtils.getApp().startActivity(IntentUtils.getInstallAppIntent(file, authority))
    }

    /**
     * 获取APP签名
     */
    fun getSignature(): String {
        try {
            var spSignature = SharedPreferencesConfigUtils.getInstance().getString(SharedPreferencesConfigUtils.SIGNATURE)
            if (!spSignature.isNullOrEmpty()) {
                return spSignature
            }
            val pm: PackageManager = BaseUtils.getApp().packageManager
            val apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES)
            for (packageInfo in apps) {
                if (packageInfo.packageName.contains(BaseUtils.getApp().packageName)) {

                    spSignature = MD5Utils.getMD5(packageInfo.signatures[0].toByteArray())
                    SharedPreferencesConfigUtils.getInstance().setString(SharedPreferencesConfigUtils.SIGNATURE, spSignature)
                    return spSignature
                }
            }

        } catch (e: Exception) {
            YLogUtils.e("getSignature", e.message)
        }

        return "unKnow"
    }

    fun relaunchApp() {
        relaunchApp(false)
    }

    /**
     * 重启
     * @param isKillProcess 如果为True，则终止进程，否则为false。
     */
    fun relaunchApp(isKillProcess: Boolean) {
        val intent = getLaunchAppIntent(BaseUtils.getApp().packageName, true)
        if (intent == null) {
            YLogUtils.e("AppUtils--Didn't exist launcher activity.")
            return
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        BaseUtils.getApp().startActivity(intent)
        if (!isKillProcess) return
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    private fun getLaunchAppIntent(packageName: String): Intent? {
        return getLaunchAppIntent(packageName, false)
    }

    private fun getLaunchAppIntent(packageName: String, isNewTask: Boolean): Intent? {
        val launcherActivity = getLauncherActivity(packageName)
        if (!launcherActivity.isEmpty()) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName(packageName, launcherActivity)
            intent.component = cn
            return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
        }
        return null
    }

    private fun getLauncherActivity(pkg: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pkg)
        val pm = BaseUtils.getApp().packageManager
        val info = pm.queryIntentActivities(intent, 0)
        val size = info.size
        if (size == 0) return ""
        for (i in 0 until size) {
            val ri = info.get(i)
            if (ri.activityInfo.processName == pkg) {
                return ri.activityInfo.name
            }
        }
        return info[0].activityInfo.name
    }
}