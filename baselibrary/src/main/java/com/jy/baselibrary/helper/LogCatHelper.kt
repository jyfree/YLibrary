package com.jy.baselibrary.helper

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.baselibrary.utils.YLogUtils.eTag
import com.jy.baselibrary.utils.YLogUtils.iTag
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Administrator
 * created at 2019/10/18 17:00
 * TODO:log收集
 * 注：若path为空则保存到sdcard（需要访问权限）
 */
class LogCatHelper private constructor(mContext: Context, path: String) {
    private var dirPath: String? = null//保存路径
    private val appId: Int//应用pid
    private var logThread: Thread? = null
    /**
     * 启动log日志保存
     */
    fun start() {
        if (!YLogUtils.SHOW_LOG) return
        if (logThread == null) {
            logThread = Thread(LogRunnable(appId, dirPath))
        }
        logThread?.start()
    }

    private class LogRunnable internal constructor(pid: Int, dirPath: String?) :
        Runnable {
        private var mProcess: Process? = null
        private var fos: FileOutputStream? = null
        private var mReader: BufferedReader? = null
        private val cmd: String
        private val mPid: String
        var dirPath: String?
        var fileName: String? = null
        override fun run() {
            try {
                filter(dirPath, fileName)
                mProcess = Runtime.getRuntime().exec(cmd)
                mReader = BufferedReader(
                    InputStreamReader(mProcess?.inputStream),
                    1024
                )
                var line: String
                while (mReader!!.readLine().also { line = it } != null) {
                    if (line.isEmpty()) {
                        continue
                    }
                    if (fos != null && line.contains(mPid)) {
                        fos!!.write((FormatDate.formatTime + " " + line + "\r\n").toByteArray())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mProcess?.destroy()
                mProcess = null
                try {
                    mReader?.close()
                    mReader = null
                    fos?.close()
                    fos = null
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }

        init {
            mPid = "" + pid
            this.dirPath = dirPath
            try {
                fileName = FormatDate.formatDate
                val file = File(dirPath, "$fileName.log")
                if (!file.exists()) {
                    file.createNewFile()
                }
                fos = FileOutputStream(file, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            cmd = "logcat *:v | grep \"($mPid)\""
        }
    }

    private object FormatDate {
        val formatDate: String
            get() {
                val sdf = SimpleDateFormat("yyyyMMddHH", Locale.CHINA)
                return sdf.format(System.currentTimeMillis())
            }

        val formatTime: String
            get() {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                return sdf.format(System.currentTimeMillis())
            }
    }

    companion object {
        private var instance: LogCatHelper? = null
        private val TAG = LogCatHelper::class.java.simpleName
        /**
         * @param path log日志保存根目录
         */
        fun getInstance(mContext: Context, path: String): LogCatHelper? {
            if (instance == null) {
                instance = LogCatHelper(mContext, path)
            }
            return instance
        }

        private fun filter(dirPath: String?, nowFileName: String?) {
            try {
                var nowTime = 0
                if (null != nowFileName && nowFileName.isNotEmpty()) {
                    nowTime = nowFileName.substring(0, nowFileName.length - 2).toInt()
                    iTag(
                        TAG,
                        "创建log文件",
                        nowFileName,
                        "当前时间",
                        nowTime
                    )
                }
                val root = File(dirPath)
                val fileList = root.listFiles()
                if (fileList == null || fileList.isEmpty()) {
                    return
                }
                iTag(TAG, "过滤旧log文件，只保留当天log")
                for (file in fileList) {
                    val name = file.name
                    if (name.isNotEmpty() && name.endsWith(".log")) {
                        val time = name.substring(0, name.length - 6).toInt()
                        if (nowTime - time > 0) {
                            val success = file.delete()
                            iTag(TAG, "删除文件", name, "删除成功", success)
                        } else {
                            iTag(TAG, "保留文件", name)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                eTag(TAG, "过滤log失败", e.message)
            }
        }
    }

    init {
        appId = android.os.Process.myPid()
        dirPath = if (TextUtils.isEmpty(path)) {
            (Environment.getExternalStorageDirectory().absolutePath + File.separator + "seeker" + File.separator + mContext.packageName)
        } else {
            path
        }
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }
}